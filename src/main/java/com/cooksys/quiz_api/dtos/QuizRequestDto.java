package com.cooksys.quiz_api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data

public class QuizRequestDto {

    private String name;
    private List<QuestionRequestDto> questions;
}
