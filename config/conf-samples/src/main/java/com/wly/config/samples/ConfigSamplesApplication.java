package com.wly.config.samples;

import com.wly.config.core.annotation.EnableConf;
import com.wly.config.core.annotation.EnableRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConf
@EnableRegistry
public class ConfigSamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigSamplesApplication.class, args);
	}

}
