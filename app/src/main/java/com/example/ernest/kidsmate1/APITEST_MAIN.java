package com.example.ernest.kidsmate1;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class APITEST_MAIN extends AppCompatActivity {

    private static final String TAG = APITEST_MAIN.class.getSimpleName();
    private APITEST_VoiceRecognizer mVoiceRecognizer;
    private InnerEventHandler mInnerEventHandler;
    private Button button;
    private TextView textView;
    ///////////////////////////////////////////
    private APITEST_SpeechService mSpeechService;
    private APITEST_VoiceRecorder mVoiceRecorder;

    private int mColorHearing;
    private int mColorNotHearing;
    private TextView mStatus;
    private TextView mText;
    private ResultAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private static final String STATE_RESULTS = "results";

    private final APITEST_VoiceRecorder.Callback mVoiceCallback = new APITEST_VoiceRecorder.Callback() {
        @Override
        public void onVoiceStart() {
            showStatus(true);
            if (mSpeechService != null) {
                mSpeechService.startRecognizing(mVoiceRecorder.getSampleRate());
            }
        }

        @Override
        public void onVoice(byte[] data, int size) {
            if (mSpeechService != null) {
                mSpeechService.recognize(data, size);
            }
        }

        @Override
        public void onVoiceEnd() {
            showStatus(false);
            if (mSpeechService != null) {
                mSpeechService.finishRecognizing();
            }
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            mSpeechService = APITEST_SpeechService.from(binder);
            mSpeechService.addListener(mSpeechServiceListener);
            mStatus.setVisibility(View.VISIBLE);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mSpeechService = null;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apitest_activity_main);

        mInnerEventHandler = new InnerEventHandler(this);
        mVoiceRecognizer = APITEST_VoiceRecognizer.getInstance(this);
        button = (Button) findViewById(R.id.APITEST_button);
        textView = (TextView) findViewById(R.id.APITEST_textView);
        mStatus = (TextView) findViewById(R.id.APITEST_status);
        mText = (TextView) findViewById(R.id.APITEST_text);
        final Resources resources = getResources();
        final Resources.Theme theme = getTheme();
        mColorHearing = ResourcesCompat.getColor(resources, R.color.APITEST_status_hearing, theme);
        mColorNotHearing = ResourcesCompat.getColor(resources, R.color.APITEST_status_not_hearing, theme);
        mRecyclerView = (RecyclerView) findViewById(R.id.APITEST_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ArrayList<String> results = savedInstanceState == null ? null :
                savedInstanceState.getStringArrayList(STATE_RESULTS);
        mAdapter = new ResultAdapter(results);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onButtonClicked(View v) {
        if(!mVoiceRecognizer.isRunning()) {
            button.setText("Connecting...");
            mVoiceRecognizer.recognize();
        } else {
            button.setEnabled(false);
            mVoiceRecognizer.stop();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, APITEST_SpeechService.class), mServiceConnection, BIND_AUTO_CREATE);
        startVoiceRecorder();
        mVoiceRecognizer.initialize(mInnerEventHandler);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVoiceRecognizer.release();
        stopVoiceRecorder();

        // Stop Cloud Speech API
        mSpeechService.removeListener(mSpeechServiceListener);
        unbindService(mServiceConnection);
        mSpeechService = null;
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null) {
            outState.putStringArrayList(STATE_RESULTS, mAdapter.getResults());
        }
    }

    private void startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
        }
        mVoiceRecorder = new APITEST_VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();
    }

    private void stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }

    private void showStatus(final boolean hearingVoice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatus.setTextColor(hearingVoice ? mColorHearing : mColorNotHearing);
            }
        });
    }

    private final APITEST_SpeechService.Listener mSpeechServiceListener =
            new APITEST_SpeechService.Listener() {
                @Override
                public void onSpeechRecognized(final String text, final boolean isFinal) {
                    if (isFinal) {
                        mVoiceRecorder.dismiss();
                    }
                    if (mText != null && !TextUtils.isEmpty(text)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFinal) {
                                    mText.setText(null);
                                    mAdapter.addResult(text);
                                    mRecyclerView.smoothScrollToPosition(0);
                                } else {
                                    mText.setText(text);
                                }
                            }
                        });
                    }
                }
            };

    private static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.apitest_item, parent, false));
            text = (TextView) itemView.findViewById(R.id.APITEST_textView2);
        }

    }

    private static class ResultAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final ArrayList<String> mResults = new ArrayList<>();

        ResultAdapter(ArrayList<String> results) {
            if (results != null) {
                mResults.addAll(results);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(mResults.get(position));
        }

        @Override
        public int getItemCount() {
            return mResults.size();
        }

        void addResult(String result) {
            mResults.add(0, result);
            notifyItemInserted(0);
        }

        public ArrayList<String> getResults() {
            return mResults;
        }

    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                button.setText("Connected");
                break;
            case R.id.audioRecording:
                break;
            case R.id.partialResult:
                String partialResult = (String) msg.obj;
                textView.setText(partialResult);
                break;
            case R.id.endPointDetected:
                break;
            case R.id.finalResult:
                List<String> results = ((SpeechRecognitionResult)(msg.obj)).getResults();
                StringBuilder strBuf = new StringBuilder();
                for (String result: results) {
                    strBuf.append(result);
                    strBuf.append("\n");
                }
                textView.setText(strBuf.toString());
                break;
            case R.id.recognitionError:
                textView.setText("Error code : " + msg.obj.toString());
                button.setText("START");
                button.setEnabled(true);
                break;
            case R.id.endPointDetectTypeSelected:
                break;
            case R.id.clientInactive:
                button.setText("START");
                button.setEnabled(true);
                break;
        }
    }

    private static class InnerEventHandler extends Handler {
        // 이벤트 핸들러 이너 클래스
        private final WeakReference<APITEST_MAIN> mActivity;
        InnerEventHandler(APITEST_MAIN activity) {
            mActivity = new WeakReference(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            APITEST_MAIN activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}
