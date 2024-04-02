package com.fisa.wooriarte.exception;

public class MessageException extends Exception{

    //매개변수 없는 생성자
    public MessageException(){

    }
    public MessageException(String message){
        super(message);
    }
}
