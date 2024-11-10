package com.hellduo.global.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponse {
    private int status;
    private List<String> messages;
    @Builder
    public ErrorResponse(int status, List<String> messages){
        this.status = status;
        this.messages = messages;
    }
    public void addMessage(String message){
        this.messages.add(message);
    }
}
