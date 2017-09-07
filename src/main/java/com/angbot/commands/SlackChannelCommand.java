package com.angbot.commands;
import java.util.StringTokenizer;

import org.springframework.stereotype.Service;

import com.angbot.service.CommandApiService;


@Service
public class SlackChannelCommand extends CommCommand{
	boolean state = false;
	
	public SlackChannelCommand(CommandApiService service) {
		super(service);
	}
	@Override
	public String command() {
		return "!채널";
	}
	
	@Override
	public String help() {	
		return "해당 슬랙 채널 목록 ( 최초 가입자는 확인 필수 )";
	}
	
	@Override
	public String run(StringTokenizer token) throws Exception {
		if(!this.validation(token)){
			return "`ex) !채널 (파라매터 없음)`";
		}
		state = true;
		String result = this.service.channelList();
		state = false;
		return result;
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
