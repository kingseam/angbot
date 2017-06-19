package com.angbot.commands;

public abstract class CommCommand {
	public abstract String getCommand();

	public String[] getAliases() {
		return new String[] {};
	}

	public abstract String excute(String msg) throws Exception;

	public String getUsage(){
		return "";
	}

	public String getDescription(){
		return "";
	}

	public abstract String printHelp(String msg);

	protected void println() {

	}
}
