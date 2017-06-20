package com.angbot.commands;

import java.util.Map;
import java.util.StringTokenizer;

import com.angbot.service.SlackCommService;
import com.angbot.util.PrintToSlackUtil;

public class HelpCommand extends CommCommand{
	
	public HelpCommand(SlackCommService service) {
		super(service);
	}
	
	@Override
	public String command() {
		return "!사용법";
	}
	
	@Override
	public String run(StringTokenizer token) throws Exception {
		// TODO Auto-generated method stub
		if(!this.validation(token)){
			return "ex) !사용법 (파라매터 없음)";
		}
		return PrintToSlackUtil.printHelp().toString();
	}
	
	public boolean validation(StringTokenizer token){		
		if(token.countTokens() > 0){
			return false;
		}
		return true;
	}
}
