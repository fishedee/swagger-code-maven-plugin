package com.fishedee;

public class CrashException extends RuntimeException{
    public CrashException(String msg,Exception e){
        super(msg,e);
    }
}
