package xyz.sky731.programming.lab6;

import xyz.sky731.programming.lab3.Bredlam;

import java.io.Serializable;

public class Request implements Serializable {
    private String cmd;
    private Bredlam bredlam;
    public Request(String cmd, Bredlam bredlam) {
        this.cmd = cmd;
        this.bredlam = bredlam;
    }

    public Bredlam getBredlam() {
        return bredlam;
    }

    public String getCmd() {
        return cmd;
    }
}
