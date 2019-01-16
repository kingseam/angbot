import com.angbot.common.Wordvec;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DemoCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("CommandLineRunner Args: " + Arrays.toString(args));
        Wordvec test = new Wordvec();
        test.run();
    }

}