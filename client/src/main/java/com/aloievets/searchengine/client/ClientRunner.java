package com.aloievets.searchengine.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ClientRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRunner.class);

    @Value("${server.url}")
    private String serverUrl;

    public static void main(String[] args) {
        SpringApplication.run(ClientRunner.class);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> {
            LOGGER.info("STARTED demo client for server " + serverUrl);

            LOGGER.info("PUT document for key = zoo");
            restTemplate.put(serverUrl + "/zoo", new HttpEntity<>("monkey zebra elephant lion tiger"));
            LOGGER.info("GET document for key = zoo");
            LOGGER.info("Document is: " + restTemplate.getForObject(serverUrl + "/zoo", String.class));

            LOGGER.info("PUT document for key = large-zoo");
            restTemplate.put(serverUrl + "/large-zoo", new HttpEntity<>("monkey zebra elephant lion tiger rat turtle dolphin"));
            LOGGER.info("GET document for key = large-zoo");
            LOGGER.info("Document is: " + restTemplate.getForObject(serverUrl + "/large-zoo", String.class));

            LOGGER.info("PUT document for key = small-zoo");
            restTemplate.put(serverUrl + "/small-zoo", new HttpEntity<>("monkey elephant zebra"));
            LOGGER.info("GET document for key = small-zoo");
            LOGGER.info("Document is: " + restTemplate.getForObject(serverUrl + "/small-zoo", String.class));

            LOGGER.info("Searching for phrase = zebra elephant");
            LOGGER.info("Ids of matching documents are: " + restTemplate.getForObject(serverUrl + "?searchPhrase=zebra elephant", String.class));

            LOGGER.info("Demo client FINISHED");
        };
    }
}
