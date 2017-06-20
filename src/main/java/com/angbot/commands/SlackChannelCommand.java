package com.angbot.commands;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.stereotype.Service;
import com.angbot.service.CommandApiService;


@Service
public class SlackChannelCommand extends CommCommand{
	
	public SlackChannelCommand(CommandApiService service) {
		super(service);
	}
	@Override
	public String command() {
		return "!채널";
	}
	
	@Override
	public String run(StringTokenizer token) throws Exception {
		if(!this.validation(token)){
			return "ex) !채널 (파라매터 없음)";
		}
		
		return this.service.channelList();
	}
	
	public boolean validation(StringTokenizer token){		
		if(token.countTokens() > 0){
			return false;
		}
		return true;
	}
}
