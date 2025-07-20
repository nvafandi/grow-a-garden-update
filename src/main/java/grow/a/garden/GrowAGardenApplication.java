package grow.a.garden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GrowAGardenApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrowAGardenApplication.class, args);
	}

}
