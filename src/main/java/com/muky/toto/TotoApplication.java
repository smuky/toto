package com.muky.toto;

import com.muky.toto.config.IsraelLeagueConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(IsraelLeagueConfig.class)
public class TotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TotoApplication.class, args);
	}

}
