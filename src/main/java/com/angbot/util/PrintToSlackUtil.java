package com.angbot.util;

import com.angbot.slack.dto.ApiBaseDto;
import com.angbot.slack.dto.ApiChannelDto;
import com.angbot.slack.object.Channel;

public class PrintToSlackUtil{
	
	public static StringBuffer printChannel(ApiChannelDto dtoList){		
		StringBuffer resultMsg = new StringBuffer();
		int rank = 1;
		
		resultMsg.append("```");
		for(Channel dto : dtoList.getResponseItem()){
			resultMsg.append(String.valueOf(rank));
			resultMsg.append("위 : #"+dto.getName());
			resultMsg.append("("+dto.getSubject()+")");
			resultMsg.append("("+dto.getNum_members()+"명)");
			resultMsg.append(""+dto.getId().substring(0, dto.getId().length()-1)+"*");
			resultMsg.append("\n");
			rank++;
		}
		resultMsg.append("```");
		
		return resultMsg;
	}
	

	public static StringBuffer printHelp(){		
		StringBuffer resultMsg = new StringBuffer();		
		resultMsg.append("```\n !어디야 : 찾고자 하는 명칭의 관련된 위치를 불러온다. \n !카페최신글 : 남궁성코드초보스터디 카페 최신글을 가져온다 \n !검색 : 네이버 검색 중 유사도 높은 1개를 가져온다.\n !이미지 : 네이버 유사도 높은 이미지 1개를 가져온다.\n !사전 : 네이버 용어 사전 검색\n !유저 : 현재 접속 유저 \n !채널 : 채널 목록```");
		return resultMsg;
	}
	
}
