package com.angbot.commands;

import java.util.StringTokenizer;

import com.angbot.service.CommandApiService;
import com.angbot.util.PrintToSlackUtil;

public class NoticeCommand extends CommCommand{
	
	public NoticeCommand(CommandApiService service) {
		super(service);
	}
	
	@Override
	public String command() {
		return "!공지";
	}
	
	@Override
	public String help() {	
		return "slack 공지사항";
	}
	
	@Override
	public String run(StringTokenizer token) throws Exception {
		// TODO Auto-generated method stub
		if(!this.validation(token)){
			return "`ex) !사용법 (파라매터없음)`";
		}
		return PrintToSlackUtil.printNotice();
	}
	
	public boolean validation(StringTokenizer token){		
		if(token.countTokens() > 0){
			return false;
		}
		return true;
	}
	@Override
	public boolean isState() {
		// TODO Auto-generated method stub
		return false;
	}
}
