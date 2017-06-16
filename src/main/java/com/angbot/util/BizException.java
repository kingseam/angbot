package com.angbot.util;


public class BizException {

	private String errorCode;
    private String errorMessage;

    public BizException(CodeSlack code){
    	this.errorCode = code.getCode();
    	this.errorMessage = code.getMessage();
    }
    
    public BizException(String code, String errorMessage){
    	this.errorCode = code;
        this.errorMessage = errorMessage;
    }
    
    public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
        return errorMessage;
    }

}
