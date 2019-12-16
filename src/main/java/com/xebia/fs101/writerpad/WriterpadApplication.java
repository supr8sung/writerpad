package com.xebia.fs101.writerpad;

import com.xebia.fs101.writerpad.service.CustomUserDetailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.client.RestTemplate;

@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@SpringBootApplication
@EnableAsync
@EnableFeignClients
public class WriterpadApplication {
    public static void main(String[] args) {

        SpringApplication.run(WriterpadApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {

        return new RestTemplate();
    }

}
