package com.angbot.commands;

import java.util.StringTokenizer;

import com.angbot.service.CommandApiService;

public class RecipeCommand extends CommCommand{

	public RecipeCommand(CommandApiService service) {
		super(service);
	}

	@Override
	public String command() {
		return "!레시피";
	}

	@Override
	public String help() {
		return "레시피 재료찾기";
	}

	@Override
	public String run(StringTokenizer token) throws Exception {
		if(!this.validation(token)){
			return "`ex) !레시피 음식명`";
		}

		return service.searchRecipe(token);
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
