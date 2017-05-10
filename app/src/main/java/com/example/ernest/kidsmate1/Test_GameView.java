package com.example.ernest.kidsmate1;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Test_GameView extends View {
    /////////////////////////////////////// 객체 목록
    private MyImageView myCharacter;
    private MyButton mic;
    private MyButton pass;
    private MyButton play;
    private MyButton submit;
    private MyButton input;
    private MyTextView quizBlankGuessing;
    private MyTextView hint;
    private MyImageView enemy;
    private MyImageView quizImageGuessing;
    private MyTextView quizWordChain;
    //////////////////////////////////////

    /////////////////////////////////////////////// myCharacter 정보
    private final int myCharacterRatioInView = 40;
    private int myCharacterRatioChange;
    private int myCharacterX = 0, myCharacterY = 0;
    private int myCharacterWidth = 0, myCharacterHeight = 0;
    private Bitmap myCharacterResizedImage;
    ////////////////////////////////////////////////

    /////////////////////////////////////////////// mic 정보
    private final int micRatioInView = 10;
    private int micRatioChange;
    private int micX = 0, micY = 0;
    private int micWidth = 0, micHeight = 0;
    private Bitmap micResizedImage;
    private Paint micTextPaint;
    private int micTextX = 0, micTextY = 0;
    ///////////////////////////////////////////////

    /////////////////////////////////////////////// pass 정보
    private final int passRatioInView = 10;
    private int passRatioChange;
    private int passX = 0, passY = 0;
    private int passWidth = 0, passHeight = 0;
    private Bitmap passResizedImage;
    private Paint passTextPaint;
    private int passTextX = 0, passTextY = 0;
    ///////////////////////////////////////////////

    /////////////////////////////////////////////// play 정보
    private final int playRatioInView = 10;
    private int playRatioChange;
    private int playX = 0, playY = 0;
    private int playWidth = 0, playHeight = 0;
    private Bitmap playResizedImage;
    private Paint playTextPaint;
    private int playTextX = 0, playTextY = 0;
    ///////////////////////////////////////////////

    /////////////////////////////////////////////// submit 정보
    private final int submitRatioInView = 10;
    private int submitRatioChange;
    private int submitX = 0, submitY = 0;
    private int submitWidth = 0, submitHeight = 0;
    private Bitmap submitResizedImage;
    private Paint submitTextPaint;
    private int submitTextX = 0, submitTextY = 0;
    ///////////////////////////////////////////////

    /////////////////////////////////////////////// input 정보
    private final int inputRatioInView = 10;
    private int inputRatioChange;
    private int inputX = 0, inputY = 0;
    private int inputWidth = 0, inputHeight = 0;
    private Bitmap inputResizedImage;
    private Paint inputTextPaint;
    private int inputTextX = 0, inputTextY = 0;
    ///////////////////////////////////////////////

    /////////////////////////////////////////////// quizBlankGuessing 정보
    private Paint quizBlankGuessingTextPaint;
    private int quizBlankGuessingTextX = 0, quizBlankGuessingTextY = 0;
    ///////////////////////////////////////////////

    /////////////////////////////////////////////// hint 정보
    private TextPaint hintTextPaint;
    private int hintTextX = 0, hintTextY = 0;
    private StaticLayout hintStaticLayout;
    ///////////////////////////////////////////////

    /////////////////////////////////////////////// enemy 정보
    private final int enemyRatioInView = 30;
    private int enemyRatioChange;
    private int enemyX = 0, enemyY = 0;
    private int enemyWidth = 0, enemyHeight = 0;
    private Bitmap enemyResizedImage;
    ////////////////////////////////////////////////

    /////////////////////////////////////////////// quizImageGuessing 정보
    private final int quizImageGuessingRatioInView = 35;
    private int quizImageGuessingRatioChange;
    private int quizImageGuessingX = 0, quizImageGuessingY = 0;
    private int quizImageGuessingWidth = 0, quizImageGuessingHeight = 0;
    private Bitmap quizImageGuessingResizedImage;
    ////////////////////////////////////////////////

    /////////////////////////////////////////////// quizWordChain 정보
    private TextPaint quizWordChainTextPaint;
    private int quizWordChainTextX = 0, quizWordChainTextY = 0;
    StaticLayout quizWordChainStaticLayout;
    ///////////////////////////////////////////////

    //////////////////////////////////////////// view 정보
    private int view_width, view_height;
    private Context context;
    ///////////////////////////////////////////

    ///////////////////////////////////////////////// 생성자
    public Test_GameView(Context context) {
        super(context);
        this.context = context;
        myCharacter = new MyImageView(context, this);
        mic = new MyButton(context, this);
        micInit();
        pass = new MyButton(context, this);
        passInit();
        play = new MyButton(context, this);
        playInit();
        submit = new MyButton(context, this);
        submitInit();
        input = new MyButton(context, this);
        inputInit();
        quizBlankGuessing = new MyTextView(context, this);
        quizBlankGuessingInit();
        hint = new MyTextView(context, this);
        hintInit();
        enemy = new MyImageView(context, this);
        quizImageGuessing = new MyImageView(context, this);
        quizWordChain = new MyTextView(context, this);
        quizWordChainInit();
    }
    /////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////// 화면 그림
    @Override
    protected void onDraw(Canvas canvas) {
        if(myCharacter.getImage() != null && view_width != 0 && view_height != 0) {
            myCharacterResize();
        }
        if(myCharacterResizedImage != null && view_width != 0 && view_height != 0) {
            canvas.drawBitmap(myCharacterResizedImage, myCharacterX, myCharacterY, null);
        }
        if(mic.getImage() != null && view_width != 0 && view_height != 0) {
            micResize();
        }
        if(micResizedImage != null && view_width != 0 && view_height != 0) {
            canvas.drawBitmap(micResizedImage, micX, micY, null);
            canvas.drawText(mic.getText(), micTextX, micTextY, micTextPaint);
        }
        if(pass.getImage() != null && view_width != 0 && view_height != 0) {
            passResize();
        }
        if(passResizedImage != null && view_width != 0 && view_height != 0) {
            canvas.drawBitmap(passResizedImage, passX, passY, null);
            canvas.drawText(pass.getText(), passTextX, passTextY, passTextPaint);
        }
        if(play.getImage() != null && view_width != 0 && view_height != 0) {
            playResize();
        }
        if(playResizedImage != null && view_width != 0 && view_height != 0) {
            canvas.drawBitmap(playResizedImage, playX, playY, null);
            canvas.drawText(play.getText(), playTextX, playTextY, playTextPaint);
        }
        if(input.getImage() != null && view_width != 0 && view_height != 0) {
            inputResize();
        }
        if(inputResizedImage != null && view_width != 0 && view_height != 0) {
            canvas.drawBitmap(inputResizedImage, inputX, inputY, null);
            canvas.drawText(input.getText(), inputTextX, inputTextY, inputTextPaint);
        }
        if(submit.getImage() != null && view_width != 0 && view_height != 0) {
            submitResize();
        }
        if(submitResizedImage != null && view_width != 0 && view_height != 0) {
            canvas.drawBitmap(submitResizedImage, submitX, submitY, null);
            canvas.drawText(submit.getText(), submitTextX, submitTextY, submitTextPaint);
        }
        if(hint.getText() != "" && view_width != 0 && view_height != 0) {
            hintResize();
        }
        hintStaticLayout = new StaticLayout(hint.getText(), hintTextPaint, 500, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        canvas.save();
        canvas.translate(hintTextX, hintTextY);
        hintStaticLayout.draw(canvas);
        canvas.restore();
        if(enemy.getImage() != null && view_width != 0 && view_height != 0) {
            enemyResize();
        }
        if(enemyResizedImage != null && view_width != 0 && view_height != 0) {
            canvas.drawBitmap(enemyResizedImage, enemyX, enemyY, null);
        }
        if(quizImageGuessing.getImage() != null && view_width != 0 && view_height != 0) {
            quizImageGuessingResize();
        }
        if(quizImageGuessingResizedImage != null && view_width != 0 && view_height != 0) {
            canvas.drawBitmap(quizImageGuessingResizedImage, quizImageGuessingX, quizImageGuessingY, null);
        }
        if(quizBlankGuessing.getText() != "" && view_width != 0 && view_height != 0) {
            quizBlankGuessingResize();
        }
        canvas.drawText(quizBlankGuessing.getText(), quizBlankGuessingTextX, quizBlankGuessingTextY, quizBlankGuessingTextPaint);
        if(quizWordChain.getText() != "" && view_width != 0 && view_height != 0) {
            quizWordChainResize();
        }
        quizWordChainStaticLayout = new StaticLayout(quizWordChain.getText(), quizWordChainTextPaint, 400, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        canvas.save();
        canvas.translate(quizWordChainTextX, quizWordChainTextY);
        quizWordChainStaticLayout.draw(canvas);
        canvas.restore();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////// 화면 바뀔때 resizing
    @Override
    protected void onSizeChanged(int view_width, int view_height, int old_view_width, int old_view_height) {
        super.onSizeChanged(view_width, view_height, old_view_width, old_view_height);
        this.view_width = view_width;
        this.view_height = view_height;

        if(myCharacter.getImage() != null && view_width != 0 && view_height != 0) {
            myCharacterResize();
        }
        if(mic.getImage() != null && view_width != 0 && view_height != 0) {
            micResize();
        }
        if(pass.getImage() != null && view_width != 0 && view_height != 0) {
            passResize();
        }
        if(play.getImage() != null && view_width != 0 && view_height != 0) {
            playResize();
        }
        if(submit.getImage() != null && view_width != 0 && view_height != 0) {
            submitResize();
        }
        if(input.getImage() != null && view_width != 0 && view_height != 0) {
            inputResize();
        }
        quizBlankGuessingResize();
        hintResize();
        if(enemy.getImage() != null && view_width != 0 && view_height != 0) {
            enemyResize();
        }
        if(quizImageGuessing.getImage() != null && view_width != 0 && view_height != 0) {
            quizImageGuessingResize();
        }
        quizWordChainResize();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////// touch envent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isTouch = false;

        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                isTouch = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                isTouch = false;
                break;
            default:
                return true;
        }

        int ptrIdx = MotionEventCompat.getActionIndex(event);
        int id = MotionEventCompat.getPointerId(event, ptrIdx);

        float x = MotionEventCompat.getX(event, ptrIdx);
        float y = MotionEventCompat.getY(event, ptrIdx);

        //////////////////////////////////////////////////////// 버튼 action
        mic.action(id, isTouch, x, y);
        pass.action(id, isTouch, x, y);
        play.action(id, isTouch, x, y);
        submit.action(id, isTouch, x, y);
        input.action(id, isTouch, x, y);
        /////////////////////////////////////////////////////////

        return true;
    }
    ///////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////// 객체 resizing
    private void myCharacterResize() {
        if(myCharacter.getImage().getHeight() > myCharacter.getImage().getWidth()) {
            myCharacterHeight = view_height * myCharacterRatioInView / 100;
            myCharacterRatioChange = myCharacterHeight * 100 / myCharacter.getImage().getHeight();
            myCharacterWidth = myCharacter.getImage().getWidth() * myCharacterRatioChange / 100;
        }
        else {
            myCharacterWidth = view_width * myCharacterRatioInView / 100;
            myCharacterRatioChange = myCharacterWidth * 100 / myCharacter.getImage().getWidth();
            myCharacterHeight = myCharacter.getImage().getHeight() * myCharacterRatioChange / 100;
        }
        myCharacterX = view_width / 2 - myCharacterWidth / 2;
        myCharacterY = view_height - view_height / 8 - myCharacterHeight;
        myCharacterResizedImage = Bitmap.createScaledBitmap(myCharacter.getImage(), myCharacterWidth, myCharacterHeight, true);
    }
    private void micResize() {
        if(mic.getImage().getHeight() > mic.getImage().getWidth()) {
            micHeight = view_height * micRatioInView / 100;
            micRatioChange = micHeight * 100 / mic.getImage().getHeight();
            micWidth = mic.getImage().getWidth() * micRatioChange / 100;
        }
        else {
            micWidth = view_width * micRatioInView / 100;
            micRatioChange = micWidth * 100 / mic.getImage().getWidth();
            micHeight = mic.getImage().getHeight() * micRatioChange / 100;
        }
        micX = myCharacterX + myCharacterWidth - micWidth / 2;
        micY = myCharacterY + myCharacterHeight - micHeight / 2;
        micResizedImage = Bitmap.createScaledBitmap(mic.getImage(), micWidth, micHeight, true);
        mic.setRectF(new RectF(micX, micY, micX + micWidth, micY + micHeight));
        micTextX = micX;
        micTextY = micY + micHeight;
    }
    private void passResize() {
        if(pass.getImage().getHeight() > pass.getImage().getWidth()) {
            passHeight = view_height * passRatioInView / 100;
            passRatioChange = passHeight * 100 / pass.getImage().getHeight();
            passWidth = pass.getImage().getWidth() * passRatioChange / 100;
        }
        else {
            passWidth = view_width * passRatioInView / 100;
            passRatioChange = passWidth * 100 / pass.getImage().getWidth();
            passHeight = pass.getImage().getHeight() * passRatioChange / 100;
        }
        passX = view_width - passWidth - passWidth / 2;
        passY = myCharacterY + myCharacterHeight - passHeight / 2;
        passResizedImage = Bitmap.createScaledBitmap(pass.getImage(), passWidth, passHeight, true);
        pass.setRectF(new RectF(passX, passY, passX + passWidth, passY + passHeight));
        passTextX = passX;
        passTextY = passY + passHeight;
    }
    private void playResize() {
        if(play.getImage().getHeight() > play.getImage().getWidth()) {
            playHeight = view_height * playRatioInView / 100;
            playRatioChange = playHeight * 100 / play.getImage().getHeight();
            playWidth = play.getImage().getWidth() * playRatioChange / 100;
        }
        else {
            playWidth = view_width * playRatioInView / 100;
            playRatioChange = playWidth * 100 / play.getImage().getWidth();
            playHeight = play.getImage().getHeight() * playRatioChange / 100;
        }
        playX = micX + micWidth + (passX - micX - micWidth) / 2 - playWidth / 2;
        playY = myCharacterY + myCharacterHeight - playHeight / 2;
        playResizedImage = Bitmap.createScaledBitmap(play.getImage(), playWidth, playHeight, true);
        play.setRectF(new RectF(playX, playY, playX + playWidth, playY + playHeight));
        playTextX = playX;
        playTextY = playY + playHeight;
    }
    private void submitResize() {
        if(submit.getImage().getHeight() > submit.getImage().getWidth()) {
            submitHeight = view_height * submitRatioInView / 100;
            submitRatioChange = submitHeight * 100 / submit.getImage().getHeight();
            submitWidth = submit.getImage().getWidth() * submitRatioChange / 100;
        }
        else {
            submitWidth = view_width * submitRatioInView / 100;
            submitRatioChange = submitWidth * 100 / submit.getImage().getWidth();
            submitHeight = submit.getImage().getHeight() * submitRatioChange / 100;
        }
        submitX = myCharacterX - submitWidth;
        submitY = myCharacterY + myCharacterHeight - submitHeight / 2;
        submitResizedImage = Bitmap.createScaledBitmap(submit.getImage(), submitWidth, submitHeight, true);
        submit.setRectF(new RectF(submitX, submitY, submitX + submitWidth, submitY + submitHeight));
        submitTextX = submitX + submitWidth / 2;
        submitTextY = submitY + submitHeight * 3 / 5;
        submitTextPaint.setTextSize(submitHeight / 3);
    }
    private void inputResize() {
        inputWidth = submitX - 10;
        inputHeight = submitHeight;
        inputX = submitX - inputWidth + submitWidth *2 / 5;
        inputY = myCharacterY + myCharacterHeight - inputHeight / 2;
        inputResizedImage = Bitmap.createScaledBitmap(input.getImage(), inputWidth, inputHeight, true);
        input.setRectF(new RectF(inputX, inputY, inputX + inputWidth, inputY + inputHeight));
        inputTextX = inputX + 2;
        inputTextY = inputY + inputHeight * 3 / 5;
        inputTextPaint.setTextSize(inputHeight / 2);
    }
    private void quizBlankGuessingResize() {
        quizBlankGuessingTextX = view_width / 2;
        quizBlankGuessingTextY = view_height / 4;
    }
    private void hintResize() {
        hintTextX = 10;
        hintTextY = 10;
    }
    private void enemyResize() {
        if(enemy.getImage().getHeight() > enemy.getImage().getWidth()) {
            enemyHeight = view_height * enemyRatioInView / 100;
            enemyRatioChange = enemyHeight * 100 / enemy.getImage().getHeight();
            enemyWidth = enemy.getImage().getWidth() * enemyRatioChange / 100;
        }
        else {
            enemyWidth = view_width * enemyRatioInView / 100;
            enemyRatioChange = enemyWidth * 100 / enemy.getImage().getWidth();
            enemyHeight = enemy.getImage().getHeight() * enemyRatioChange / 100;
        }
        enemyX = (view_width + myCharacterX + myCharacterWidth) / 2 - enemyWidth / 2;
        enemyY = view_height / 3;
        enemyResizedImage = Bitmap.createScaledBitmap(enemy.getImage(), enemyWidth, enemyHeight, true);
    }
    private void quizImageGuessingResize() {
        if(quizImageGuessing.getImage().getHeight() > quizImageGuessing.getImage().getWidth()) {
            quizImageGuessingHeight = view_height * quizImageGuessingRatioInView / 100;
            quizImageGuessingRatioChange = quizImageGuessingHeight * 100 / quizImageGuessing.getImage().getHeight();
            quizImageGuessingWidth = quizImageGuessing.getImage().getWidth() * quizImageGuessingRatioChange / 100;
        }
        else {
            quizImageGuessingWidth = view_width * quizImageGuessingRatioInView / 100;
            quizImageGuessingRatioChange = quizImageGuessingWidth * 100 / quizImageGuessing.getImage().getWidth();
            quizImageGuessingHeight = quizImageGuessing.getImage().getHeight() * quizImageGuessingRatioChange / 100;
        }
        quizImageGuessingX = view_width / 2 - quizImageGuessingWidth / 2;
        quizImageGuessingY = myCharacterY / 2 - quizImageGuessingHeight / 2;
        quizImageGuessingResizedImage = Bitmap.createScaledBitmap(quizImageGuessing.getImage(), quizImageGuessingWidth, quizImageGuessingHeight, true);
    }
    private void quizWordChainResize() {
        quizWordChainTextX = view_width / 2 - 200;
        quizWordChainTextY = view_height / 10;
    }
    ////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////// 객체 반환
    public MyImageView getMyCharacter() {
        return myCharacter;
    }
    public MyButton getMic() {
        return mic;
    }
    public MyButton getPass() {
        return pass;
    }
    public MyButton getPlay() {
        return play;
    }
    public MyButton getSubmit() {
        return submit;
    }
    public MyButton getInput() {
        return input;
    }
    public MyTextView getQuizBlankGuessing() {
        return quizBlankGuessing;
    }
    public MyTextView getHint() {
        return hint;
    }
    public MyImageView getEnemy() {
        return enemy;
    }
    public MyImageView getQuizImageGuessing() {
        return quizImageGuessing;
    }
    public MyTextView getQuizWordChain() {
        return quizWordChain;
    }
    ////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////// 객체 초기화
    private void micInit() {
        micTextPaint = new Paint();
        micTextPaint.setColor(Color.BLACK);
        micTextPaint.setTextSize(20);
        micTextPaint.setTextAlign(Paint.Align.CENTER);
    }
    private void passInit() {
        passTextPaint = new Paint();
        passTextPaint.setColor(Color.BLACK);
        passTextPaint.setTextSize(20);
        passTextPaint.setTextAlign(Paint.Align.CENTER);
    }
    private void playInit() {
        playTextPaint = new Paint();
        playTextPaint.setColor(Color.BLACK);
        playTextPaint.setTextSize(20);
        playTextPaint.setTextAlign(Paint.Align.CENTER);
    }
    private void submitInit() {
        submitTextPaint = new Paint();
        submitTextPaint.setColor(Color.BLACK);
        submitTextPaint.setTextAlign(Paint.Align.CENTER);
    }
    private void inputInit() {
        input.setText("");
        inputTextPaint = new Paint();
        inputTextPaint.setColor(Color.BLACK);
        input.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });
    }
    private void quizBlankGuessingInit() {
        quizBlankGuessingTextPaint = new Paint();
        quizBlankGuessingTextPaint.setColor(Color.GREEN);
        quizBlankGuessingTextPaint.setTextSize(100);
        quizBlankGuessingTextPaint.setTextScaleX(1.5f);
        quizBlankGuessingTextPaint.setFakeBoldText(true);
        quizBlankGuessingTextPaint.setStrokeJoin(Paint.Join.ROUND);
        quizBlankGuessingTextPaint.setTextAlign(Paint.Align.CENTER);
    }
    private void hintInit() {
        hintTextPaint = new TextPaint();
        hintTextPaint.setColor(Color.BLACK);
        hintTextPaint.setTextSize(30);
        hintTextPaint.setStrokeJoin(Paint.Join.ROUND);
    }
    private void quizWordChainInit() {
        quizWordChainTextPaint = new TextPaint();
        quizWordChainTextPaint.setColor(Color.GREEN);
        quizWordChainTextPaint.setTextSize(50);
        quizWordChainTextPaint.setFakeBoldText(true);
        quizWordChainTextPaint.setStrokeJoin(Paint.Join.ROUND);
    }
    ////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////// inputDialog 실행
    private void ShowDialog() {
        LayoutInflater dialog = LayoutInflater.from(context);
        final View dialogLayout = dialog.inflate(R.layout.input_dialog, null);
        final Dialog myDialog = new Dialog(context);

        myDialog.setTitle("답을 입력하세요");
        myDialog.setContentView(dialogLayout);
        myDialog.show();

        Button btn_ok = (Button)dialogLayout.findViewById(R.id.input_dialog_ok);
        Button btn_cancel = (Button)dialogLayout.findViewById(R.id.input_dialog_cancel);
        final EditText editText = (EditText)dialogLayout.findViewById(R.id.input_dialog);

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                input.setText(editText.getText().toString());
                myDialog.cancel();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.cancel();
            }
        });
    }
    ////////////////////////////////////////////////////////////////
}
