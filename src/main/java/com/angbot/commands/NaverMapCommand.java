package com.angbot.commands;

public class NaverMapCommand extends CommCommand{
	public NaverMapCommand() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getCommand() {
		return "!어디야";
	}
	@Override
	public String getDescription() {
		return "`ex) !어디야 강남 맛집`";
	}
	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String printHelp(String msg) {
		return "`ex) !어디야 강남 맛집`";
	}	
	@Override
	public String excute(String msg) throws Exception {
		// TODO Auto-generated method stub
		return "`ok?`";
	}
}
