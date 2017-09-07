package com.angbot.commands;

import java.util.StringTokenizer;

import org.springframework.stereotype.Service;

import com.angbot.service.CommandApiService;

@Service
public class WeatherCommand extends CommCommand {
	
	private boolean status = false;
	
	public WeatherCommand(CommandApiService service) {
		super(service);
	}

	@Override
	public String command() {
		return "!오늘날씨";
	}

	@Override
	public String run(StringTokenizer token) throws Exception {
		if(!this.validation(token)){
			return "`ex) !오늘날씨 낙성대`";
		}
		//TODO -_- 나중에 뭐 지역 이름만 가지고 날씨 구하던지 그렇게..
		return this.service.getWeathers(token).toString();
	}

	private boolean validation(StringTokenizer token) {
		if(token.countTokens() <= 0){
			return false;
		}
		return true;
	}

	@Override
	public boolean isState() {
		return status;
	}

}
