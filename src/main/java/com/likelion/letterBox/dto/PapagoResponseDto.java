package com.likelion.letterBox.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PapagoResponseDto {
    private Message message;

    @Getter
    @Setter
    public static class Message {
        private Result result;
    }

    @Getter
    @Setter
    public static class Result {
        private String srcLangType;
        private String tarLangType;
        private String translatedText;
    }
}
