package howdy.lab.birthcounterbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class BirthCounterBotApplication {

    @PostConstruct
    public void init() {
        // Ilovani har doim va har qanday serverda to'g'ri ishlashi uchun JVM vaqtini UTC ga majburlaymiz
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

	public static void main(String[] args) {
		SpringApplication.run(BirthCounterBotApplication.class, args);
	}

}
