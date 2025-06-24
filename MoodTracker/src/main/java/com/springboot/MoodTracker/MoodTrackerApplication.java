package com.springboot.MoodTracker;

import com.springboot.MoodTracker.Config.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoodTrackerApplication {

	static {
		EnvLoader.loadEnv();
	}

	public static void main(String[] args) {

		SpringApplication.run(MoodTrackerApplication.class, args);
	}

}
