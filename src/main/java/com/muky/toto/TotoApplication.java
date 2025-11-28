package com.muky.toto;

import com.muky.toto.config.IsraelLeagueConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableConfigurationProperties(IsraelLeagueConfig.class)
@EnableCaching
public class TotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TotoApplication.class, args);
	}

}
