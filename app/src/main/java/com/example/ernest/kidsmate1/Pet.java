package com.example.ernest.kidsmate1;

/**
 * Created by User on 2017-05-07.
 */

public class Pet {
    private int type;
    private String cname;
    private String uname;
    private String pid;

    public Pet(int type, String uname, String pid, String cname){
        this.type = type;
        this.uname = uname;
        this.pid = pid;
        this.cname = cname;
    }

    public int getType() {
        return type;
    }

    public String getCname() {
        return cname;
    }

    public String getUname() {
        return uname;
    }

    public String getPid() {
        return pid;
    }
}
