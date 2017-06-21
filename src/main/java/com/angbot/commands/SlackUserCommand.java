package com.angbot.commands;

import java.util.StringTokenizer;

import com.angbot.service.CommandApiService;

public class SlackUserCommand extends CommCommand{
	boolean state = false;
	
	public SlackUserCommand(CommandApiService service) {
		super(service);
	}
	@Override
	public String command() {
		return "!유저";
	}	
	
	@Override
	public String help() {	
		return "해당 슬랙 현재 접속 유저 (고도화 진행중) 속도 느림.";
	}
	
	@Override
	public String run(StringTokenizer token) throws Exception {
		if(!this.validation(token)){
			return "`ex) !유저 (파라매터 없음)`";
		}		
		this.state = true;
		String msg = this.service.userList();
		this.state = false;
		return msg;
	}
	
	public boolean validation(StringTokenizer token){		
		if(token.countTokens() > 0){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isState() {
		return state;
	}
	
}
