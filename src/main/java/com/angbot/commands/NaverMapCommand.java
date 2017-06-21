package com.angbot.commands;

import java.util.StringTokenizer;

import com.angbot.service.CommandApiService;

public class NaverMapCommand extends CommCommand{
	
	public NaverMapCommand(CommandApiService service) {
		super(service);
	}
	
	@Override
	public String command() {
		return "!어디야";
	}
	
	@Override
	public String help() {	
		return "찾고자 하는 명칭의 관련된 위치를 불러온다.";
	}
	
	@Override
	public String run(StringTokenizer token) throws Exception {
		if(!this.validation(token)){
			return "`ex) !어디야 장소명`";
		}
		
		return this.service.searchMap(token);
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
