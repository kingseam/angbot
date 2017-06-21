package com.angbot.commands;

import java.util.StringTokenizer;

import com.angbot.service.CommandApiService;

public class NaverBlogCommand extends CommCommand{
	
	public NaverBlogCommand(CommandApiService service) {
		super(service);
	}
	
	@Override
	public String command() {
		return "!검색";
	}
	
	@Override
	public String help() {	
		return "네이버 검색 중 유사도 높은 1개를 가져온다.";
	}
	
	@Override
	public String run(StringTokenizer token) throws Exception {
		if(!this.validation(token)){
			return "`ex) !검색 검색명`";
		}
		
		return this.service.searchBlog(token);
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
