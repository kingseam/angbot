package com.angbot.commands;

import java.util.StringTokenizer;

import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;

import com.angbot.service.CommandApiService;

import scala.collection.Seq;

public class NLPCommand extends CommCommand{

	public NLPCommand(CommandApiService service) {
		super(service);
	}

	@Override
	public String command() {
		return "!테스트";
	}

	@Override
	public String help() {
		return "형태소 분석 테스트";
	}

	@Override
	public String run(StringTokenizer token) throws Exception {
		if(!this.validation(token)){
			return "`ex) !테스트 단어명`";
		}
		String text = "";
		while (token.hasMoreTokens()) {
			text += token.nextToken();
			if (token.countTokens() > 0)
				text += " ";
		}
		 // Normalize
	    CharSequence normalized = OpenKoreanTextProcessorJava.normalize(text);
	    System.out.println(normalized);
	    // 한국어를 처리하는 예시입니다ㅋㅋ #한국어

	    // Tokenize
	    Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
	    System.out.println(OpenKoreanTextProcessorJava.tokensToJavaStringList(tokens));
	    // [한국어, 를, 처리, 하는, 예시, 입니, 다, ㅋㅋ, #한국어]
	    System.out.println(OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens));
	    // [한국어(Noun: 0, 3), 를(Josa: 3, 1), 처리(Noun: 5, 2), 하는(Verb(하다): 7, 2), 예시(Noun: 10, 2),
	    // 입니다(Adjective(이다): 12, 3), ㅋㅋㅋ(KoreanParticle: 15, 3), #한국어(Hashtag: 19, 4)]

	    // Phrase extraction
	    //List<KoreanPhraseExtractor.KoreanPhrase> phrases = OpenKoreanTextProcessorJava.extractPhrases(tokens, true, false);
	    //System.out.println(phrases);
	    // [한국어(Noun: 0, 3), 처리(Noun: 5, 2), 처리하는 예시(Noun: 5, 7), 예시(Noun: 10, 2), #한국어(Hashtag: 18, 4)]

		return OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens).toString();
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
