package com.angbot.commands;

import java.util.Map;
import java.util.StringTokenizer;

import com.angbot.service.CommandApiService;

public class NaverImageCommand extends CommCommand{
	
	public NaverImageCommand(CommandApiService service) {
		super(service);
	}
	
	@Override
	public String command() {
		return "!이미지";
	}
	
	@Override
	public String run(StringTokenizer token) throws Exception {
		if(!this.validation(token)){
			return "`ex) !이미지 이미지명`";
		}
		
		return this.service.searchImage(token);
	}
	
	public boolean validation(StringTokenizer token){		
		if(token.countTokens() <= 0){
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
