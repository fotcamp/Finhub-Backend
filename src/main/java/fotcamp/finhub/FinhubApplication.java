package fotcamp.finhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class FinhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinhubApplication.class, args);
	}

}
