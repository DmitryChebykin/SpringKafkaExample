package com.example.springkafkaexample.service;

import com.example.springkafkaexample.dto.ChuckJokeResponseDto;

public interface ChuckNorrisIO {
    ChuckJokeResponseDto getJoke();
}
