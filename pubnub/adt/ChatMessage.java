package com.devool.ucicareconnect.pubnub.adt;

/**
 * Created by GleasonK on 7/11/15.
 *
 * ChatMessage is used to hold information that is transmitted using PubNub.
 * A message in this app has a username, message, and timestamp.
 */
public class ChatMessage {
    private String username;
    private String message;
    private long timeStamp;
    private String senderId;
    private String deviceType;
    private String msgType;


    public ChatMessage(String username, String message, long timeStamp, String senderId, String deviceType, String msgType ){
        this.username  = username;
        this.message   = message;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.deviceType = deviceType;
        this.msgType = msgType;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getMsgType() {
        return msgType;
    }
}
