package com.angbot.commands;

import java.util.StringTokenizer;

import com.angbot.service.CommandApiService;

public class NaverCafeCommand extends CommCommand{
	
	public NaverCafeCommand(CommandApiService service) {
		super(service);
	}
	
	@Override
	public String command() {
		return "!카페";
	}
	
	@Override
	public String run(StringTokenizer token) throws Exception {
		if(!this.validation(token)){
			return "`ex) !카페 (파라메터없음)`";
		}
		
		return this.service.searchCafe(token);
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
