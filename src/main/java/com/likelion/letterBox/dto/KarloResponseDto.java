package com.likelion.letterBox.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)  //받은 JSON에서 매핑할 때 내가 정의하지 않은 속성은 무시
public class KarloResponseDto {
    private String id;
    @JsonProperty("model_version")  //model_version 필드를 modelVersion에 매핑
    private String modelVersion;
    private List<ImageInfo> images;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class ImageInfo {
        private String id;
        private long seed;
        private String image;
    }
}
