package weather;

import com.angbot.common.Wordvec;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.angbot.service.CommandApiService;

public class weatherTest {

	@Test
	public void word2vec() throws Exception {
		String filePath = new ClassPathResource("raw_sentences.txt").getFile().getAbsolutePath();

		// Strip white space before and after for each line
		SentenceIterator iter = new BasicLineIterator(filePath);
		// Split on white spaces in the line to get words
		TokenizerFactory t = new DefaultTokenizerFactory();

        /*
            CommonPreprocessor will apply the following regex to each token: [\d\.:,"'\(\)\[\]|/?!;]+
            So, effectively all numbers, punctuation symbols and some special symbols are stripped off.
            Additionally it forces lower case for all tokens.
         */
		t.setTokenPreProcessor(new CommonPreprocessor());

		Word2Vec vec = new Word2Vec.Builder()
			.minWordFrequency(5)
			.iterations(1)
			.layerSize(100)
			.seed(42)
			.windowSize(5)
			.iterate(iter)
			.tokenizerFactory(t)
			.build();

		vec.fit();

		Collection<String> lst =  vec.similarWordsInVocabTo("쓰레기",0);
		System.out.println(lst.toArray());
	}
	
	@Test
	public void test1() {
		CommandApiService apiService = new CommandApiService();
		System.out.println(apiService.getWeathers(new StringTokenizer("서대문구", " ")));
	}
	
	public static void main(String[] args) {
		StringTokenizer token = new StringTokenizer("낙성대", " ");
		Document doc = null;

		String query = "";

		while (token.hasMoreTokens()) {
			query += token.nextToken();
			if (token.countTokens() > 0)
				query += " ";
		}
		
		query += "%20날씨";
		
		System.out.println("http://m.search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=" + query + "&where=m");
		
		try {
			doc = Jsoup.connect(
					"http://m.search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=" + query + "&where=m")
					.get();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (doc != null) {
			Elements weatherElements = doc.select("div.status_bx.type1");
			String weatherText = weatherElements.select("span.u_hc:eq(0)").text();
			String weatherTemperature = weatherElements.select("div.wt_text strong em").text();

			// timeLine info
			List<String> timeLineInfos = new ArrayList<>();
			doc.select("div.grap_inner").get(0).select("ul>li").forEach(e -> {
				timeLineInfos.add((e.select("span.btn_time").text().equals("") ? e.select("strong").text() + "시" : e.select("span.btn_time").text()) + " " + e.select("span.ico_status2").text() + " " + e.select("span.wt_temp>em").text()  +"℃");
			});
			
			System.out.println(weatherText + " " + weatherTemperature + "℃ \n" + timeLineInfos.toString());
		}
	}
}
