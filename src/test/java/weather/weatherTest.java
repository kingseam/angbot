package weather;

import org.junit.Test;

import com.angbot.service.CommandApiService;

public class weatherTest {
	
	@Test
	public void test1() {
		CommandApiService apiService = new CommandApiService();
		System.out.println(apiService.getWeathers());
	}
}
