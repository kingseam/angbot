package com.angbot.util;

import java.util.List;
import java.util.Map;

import com.angbot.commands.CommCommand;
import com.angbot.domain.User;
import com.angbot.service.SlackCmdCache;
import com.angbot.slack.dto.ApiBaseDto;
import com.angbot.slack.dto.ApiChannelDto;
import com.angbot.slack.object.Channel;
import com.google.common.collect.Multiset.Entry;

public class PrintToSlackUtil{
	
	public static String printChannel(ApiChannelDto dtoList){		
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
		
		return resultMsg.toString();
	}
	
	public static String printNotice(){		
		StringBuffer resultMsg = new StringBuffer();
		
		resultMsg.append("```");
		resultMsg.append("*회칙인 듯 회칙 아닌 회칙 같은 너* \n");
		resultMsg.append("1. 회원 정리는 방장 맘대로, 방장이 하고 싶을 때\n");
		resultMsg.append("2. #general 과 #announcements 는 채널 탈퇴 금지입니다\n");
		resultMsg.append("3. 회원 가입 권한은 저에게만 있습니다 - 카페 가입자만 가입 가능\n");
		resultMsg.append("4. 프로필에 코드 초보 스터디 카페의 대화명을 알 수 있도록 대화명이든 이름에든 표시 부탁드립니다 - 아니면 1의 대상이 될 수 있음.\n");
		resultMsg.append("5. 슬랙 이메일도 카페 가입 이메일로 유지 부탁드립니다. \n");
		resultMsg.append("6. 채널 개설은 자유 - 하지만 활동이 뜸할 경우는 1과 동일한 규칙 적용 \n");
		resultMsg.append("7. 가입 이메일이 카페에 안 보이면 언제든지 탈퇴당할 수 있음…ㅎㅎ \n");
		resultMsg.append("8. 그 외에는 자유 - 하지만 민원 여부(?) 및 상황에 따라 방장 맘대로 제한 가능 \n");
		
		resultMsg.append("```");
		
		return resultMsg.toString();
	}
	

	public static String printHelp(){		
		StringBuffer resultMsg = new StringBuffer();
		
		resultMsg.append("```");
		for(java.util.Map.Entry<String, Object> entry : SlackCmdCache.cmdMap.entrySet()){
			resultMsg.append("\n "+entry.getKey() + " : " +  ((CommCommand)entry.getValue()).help());
		}
		
		
		resultMsg.append("\n\n*회칙인 듯 회칙 아닌 회칙 같은 너* \n");
		resultMsg.append("1. 회원 정리는 방장 맘대로, 방장이 하고 싶을 때\n");
		resultMsg.append("2. #general 과 #announcements 는 채널 탈퇴 금지입니다\n");
		resultMsg.append("3. 회원 가입 권한은 저에게만 있습니다 - 카페 가입자만 가입 가능\n");
		resultMsg.append("4. 프로필에 코드 초보 스터디 카페의 대화명을 알 수 있도록 대화명이든 이름에든 표시 부탁드립니다 - 아니면 1의 대상이 될 수 있음.\n");
		resultMsg.append("5. 슬랙 이메일도 카페 가입 이메일로 유지 부탁드립니다. \n");
		resultMsg.append("6. 채널 개설은 자유 - 하지만 활동이 뜸할 경우는 1과 동일한 규칙 적용 \n");
		resultMsg.append("7. 가입 이메일이 카페에 안 보이면 언제든지 탈퇴당할 수 있음…ㅎㅎ \n");
		resultMsg.append("8. 그 외에는 자유 - 하지만 민원 여부(?) 및 상황에 따라 방장 맘대로 제한 가능 \n");
		
		resultMsg.append("```");
		
		return resultMsg.toString();
	}
	
	public static String printIssue(List<Map<String, Object>> items){
		StringBuffer strOpen = new StringBuffer();
		StringBuffer strClose = new StringBuffer();
		boolean open = false;
		boolean close = false;

		strOpen.append("```\n ------------ 진행 이슈 --------------\n");
		strClose.append("```\n ------------ 완료 이슈 --------------\n");
		for(Map<String, Object> map : items){
			if(map.get("state").toString().equals("open")){
				strOpen.append("url : " + map.get("html_url")+"\n");
				strOpen.append("이슈 : " + map.get("title")+"\n");
				strOpen.append("설명 : " + map.get("body")+"\n");
				strOpen.append("작성자 :" + ((Map)map.get("user")).get("login")+"\n-------------------------------------\n");
				open = true;
			}else{
				strClose.append("url : " + map.get("html_url")+"\n");
				strClose.append("이슈 : " + map.get("title")+"\n");
				strClose.append("설명 : " + map.get("body")+"\n");
				strClose.append("작성자 :" + ((Map)map.get("user")).get("login")+"\n-------------------------------------\n");
				close = true;
			}
		}
		strOpen.append("```\n");
		strClose.append("```");
		
		if(!open){
			strOpen = new StringBuffer();
		}
		if(!close){
			strClose = new StringBuffer();
		}
		
		strOpen.append(strClose);
		
		return strOpen.toString();
	}
	
	public static String printMap(List<Map<String, String>> items){
		StringBuffer resultMsg = new StringBuffer();
		
		for(Map<String, String> map : items){
			resultMsg.append("[");
			resultMsg.append("*"+map.get("title").replaceAll("\\<.*?>","")+"*");
			resultMsg.append("]\n");
			resultMsg.append("https://openapi.naver.com/v1/map/staticmap.bin?clientId=tVMlV0yBN6vVmO7VgF_g&url=http://localhost&center=");
			resultMsg.append(map.get("mapx")+","+map.get("mapy")+"&level=12&w=600&h=500&baselayer=default&crs=NHN:128&markers=");
			resultMsg.append(map.get("mapx")+","+map.get("mapy") +" ");
			/*
			resultMsg.append("\n[ 상세보기 : ");
			resultMsg.append("http://map.naver.com/?query="+map.get("address").replaceAll(" ","") + '|' + map.get("title").replaceAll("\\<.*?>","").replaceAll(" ","") +"");
			resultMsg.append("]");
			*/
		}
		
		return resultMsg.toString();
	}
	
	public static String printDocument(List<Map<String, String>> items){
		StringBuffer resultMsg = new StringBuffer();
		
		for(Map<String, String> map : items){
			resultMsg.append("*[");
			resultMsg.append(map.get("description").replaceAll("\\<.*?>",""));
			resultMsg.append("]*\n");
			resultMsg.append("[");
			resultMsg.append(map.get("link").replaceAll("\\<.*?>",""));
			resultMsg.append("]");
		}
		
		return resultMsg.toString();
	}
	
	public static String printCafe(List<Map<String, String>> items){
		StringBuffer resultMsg = new StringBuffer();
		
		for(Map<String, String> map : items){
			resultMsg.append("*[");
			resultMsg.append(map.get("title").replaceAll("\\<.*?>",""));
			resultMsg.append("]*\n");
			resultMsg.append("[");
			resultMsg.append(map.get("link").replaceAll("\\<.*?>",""));
			resultMsg.append("](코드초보스터디카페)\n");
		}
		
		return resultMsg.toString();
	}
	
	
	public static String printBlog(List<Map<String, String>> items){
		StringBuffer resultMsg = new StringBuffer();
		
		for(Map<String, String> map : items){
			resultMsg.append("*[");
			resultMsg.append(map.get("title").replaceAll("\\<.*?>",""));
			resultMsg.append("]*\n");
			resultMsg.append("[");
			resultMsg.append(map.get("link").replaceAll("\\<.*?>",""));
			resultMsg.append("](블로그)\n");
		}
		
		return resultMsg.toString();
	}
	
	public static String printImage(List<Map<String, String>> items){
		StringBuffer resultMsg = new StringBuffer();
		
		for(Map<String, String> map : items){
			resultMsg.append("*[");
			resultMsg.append(map.get("title").replaceAll("\\<.*?>",""));
			resultMsg.append("]*\n");
			resultMsg.append("[");
			resultMsg.append(map.get("link").replaceAll("\\<.*?>",""));
			resultMsg.append("]");
		}
		
		return resultMsg.toString();
	}
	
	public static String printUser(){
		StringBuffer resultMsg = new StringBuffer();
		
		int cnt = 0;
		resultMsg.append("```");
		resultMsg.append("*접속 유저 정보*\n");
		for(java.util.Map.Entry<String, User> user : SlackCmdCache.userMap.entrySet()){
			if(user.getValue().getActive().equals("active")){
				resultMsg.append(user.getValue().getNick().substring(0, user.getValue().getNick().length()-1)+"*");
				if(user.getValue().getNick().equals("angbot")){
					resultMsg.append(" => 얘는 봇 ");
				}
				if(user.getValue().getNick().equals("eminency")){
					resultMsg.append(" => 방장님 ");
				}
				if(user.getValue().getNick().equals("beginnerjsp")){
					resultMsg.append(" => 여자킬러 ");
				}
				if(user.getValue().getNick().equals("loustler")){
					resultMsg.append(" => 늪개발자");
				}
				if(user.getValue().getNick().equals("reactor")){
					resultMsg.append(" => 개발변태 ");
				}
				if(user.getValue().getNick().equals("yuaming")){
					resultMsg.append(" => 실망의 아우성");
				}
				if(user.getValue().getNick().equals("foxrain")){
					resultMsg.append(" => 봇?");
				}
				resultMsg.append("\n");
				cnt++;
			}
		}
		resultMsg.append("[ 총"+ cnt +"명의 사람이 접속중 ] ```");
		
		return resultMsg.toString();
	}
}
