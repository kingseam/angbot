package com.angbot.commands;

import java.util.StringTokenizer;

import com.angbot.service.CommandApiService;

public class GitHubIssueCommand extends CommCommand{
	boolean state = false;
	
	public GitHubIssueCommand(CommandApiService service) {
		super(service);
	}
	@Override
	public String command() {
		return "!이슈";
	}	
	
	@Override
	public String help() {	
		return "angbot GitHub 이슈사항을 가져온다";
	}
	
	@Override
	public String run(StringTokenizer token) throws Exception {
		if(!this.validation(token)){
			return "`ex) !이슈 없음|진행|종료|외 default`";
		}		
		this.state = true;
		String msg = this.service.issueList(token);
		this.state = false;
		return msg;
	}
	
	public boolean validation(StringTokenizer token){		
		if(token.countTokens() > 1){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean isState() {
		return state;
	}
	
}
