package com.serviceapp.elk.transactionService.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebClientConfig  implements WebMvcConfigurer {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

   
    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //     registry.addResourceHandler("/resources/**")
    //             .addResourceLocations("/public/", "classpath:/static/");
    // }


    
}
