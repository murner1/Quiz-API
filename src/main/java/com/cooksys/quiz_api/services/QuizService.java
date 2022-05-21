package com.cooksys.quiz_api.services;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import org.springframework.http.ResponseEntity;

public interface QuizService {

  List<QuizResponseDto> getAllQuizzes();

    QuizResponseDto createQuiz(QuizRequestDto quizRequestDto);

  QuizResponseDto deleteQuiz(Long id);

  QuizResponseDto renameQuiz(Long id, String newName);

  QuestionResponseDto randomQuestion(Long id);

  ResponseEntity<QuizResponseDto> modifiedQuestion(Long id, QuestionRequestDto questionRequestDto);

  ResponseEntity<QuestionResponseDto> deletedQuestion(Long id, Long questionID);
}
