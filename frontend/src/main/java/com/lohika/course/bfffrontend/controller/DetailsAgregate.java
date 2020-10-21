package com.lohika.course.bfffrontend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.SpanName;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/details")
public class DetailsAgregate {

    private final static Logger logger = LoggerFactory.getLogger(DetailsAgregate.class);

    @Value("${books.url}")
    private String booksUrl;

    @Value("${authors.url}")
    private String authorsUrl;

    @GetMapping
    @NewSpan("getDetails")
    public Mono<Map> getBooksAndAuthors() {
        logger.info("Get aggregated data");
        WebClient authorClient = WebClient
                .builder()
                .baseUrl(authorsUrl)
                .build();

        WebClient bookClient = WebClient
                .builder()
                .baseUrl(booksUrl)
                .build();

        Mono<Object> authors = authorClient.get().retrieve().bodyToMono(Object.class);
        Mono<Object> books = bookClient.get().retrieve().bodyToMono(Object.class);
        return authors.zipWith(books).map(t -> {
            Map<String, Object> result = new HashMap<>();
            result.put("authors", t.getT1());
            result.put("books", t.getT2());
            return result;
        });
    }
}
