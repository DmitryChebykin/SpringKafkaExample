package com.example.springkafkaexample.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "categories",
        "created_at",
        "icon_url",
        "id",
        "updated_at",
        "url",
        "value"
})
@Generated("jsonschema2pojo")
public class ChuckJokeResponseDto {
    @JsonProperty("categories")
    public List<Object> categories = null;

    @JsonProperty("created_at")
    public String createdAt;

    @JsonProperty("icon_url")
    public String iconUrl;

    @JsonProperty("id")
    public String id;

    @JsonProperty("updated_at")
    public String updatedAt;

    @JsonProperty("url")
    public String url;

    @JsonProperty("value")
    public String value;
}
