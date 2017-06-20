package com.angbot.commands;

import java.util.StringTokenizer;

import com.angbot.service.CommandApiService;
import com.angbot.util.PrintToSlackUtil;

public class HelpCommand extends CommCommand{
	
	public HelpCommand(CommandApiService service) {
		super(service);
	}
	
	@Override
	public String command() {
		return "!�궗�슜踰�";
	}
	
	@Override
	public String run(StringTokenizer token) throws Exception {
		// TODO Auto-generated method stub
		if(!this.validation(token)){
			return "`ex) !�궗�슜踰� (�뙆�씪留ㅽ꽣 �뾾�쓬)`";
		}
		return PrintToSlackUtil.printHelp();
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
