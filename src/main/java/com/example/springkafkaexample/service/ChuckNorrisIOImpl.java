package com.example.springkafkaexample.service;

import com.example.springkafkaexample.dto.ChuckJokeResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@AllArgsConstructor
public class ChuckNorrisIOImpl implements ChuckNorrisIO {
    private final WebClient webClient;

    @Override
    public ChuckJokeResponseDto getJoke() {
        return webClient
                .get().uri("https://api.chucknorris.io/jokes/random")
                .exchangeToMono(e -> e.bodyToMono(ChuckJokeResponseDto.class)).block();
    }
}
