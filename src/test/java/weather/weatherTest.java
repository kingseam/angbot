package weather;

import java.util.StringTokenizer;

import org.junit.Test;

import com.angbot.service.CommandApiService;

public class weatherTest {
	
	@Test
	public void test1() {
		CommandApiService apiService = new CommandApiService();
		System.out.println(apiService.getWeathers(new StringTokenizer("서대문구21232", " ")));
	}
}
