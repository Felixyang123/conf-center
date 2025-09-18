package com.wly.config.samples;

import com.wly.config.core.annotation.EnableConf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConf
public class ConfigSamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigSamplesApplication.class, args);
	}

}
