package start;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"start"})
public class Start1Application {
	

	public static void main(String[] args) {
		new File(AlienController.uploadDirectory).mkdir();
		SpringApplication.run(Start1Application.class, args);
	}
}
