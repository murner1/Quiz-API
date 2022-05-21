package com.cooksys.quiz_api.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.mappers.QuestionMapper;
import com.cooksys.quiz_api.mappers.QuizMapper;
import com.cooksys.quiz_api.repositories.QuizRepository;
import com.cooksys.quiz_api.repositories.QuestionRepository;
import com.cooksys.quiz_api.repositories.AnswerRepository;
import com.cooksys.quiz_api.services.QuizService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

  private final QuizRepository quizRepository;

  private final QuestionRepository questionRepository;

  private final AnswerRepository answerRepository;
  private final QuizMapper quizMapper;

  private final QuestionMapper questionMapper;

  @Override
  public List<QuizResponseDto> getAllQuizzes() {
    return quizMapper.entitiesToDtos(quizRepository.findAll());
  }

  @Override
  public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {
    Quiz quizToCreate = quizMapper.requestDtoToEntity(quizRequestDto);

    for(Question question : quizToCreate.getQuestions()){
      question.setQuiz(quizToCreate);

      for (Answer answer : question.getAnswers()){
        answer.setQuestion(question);
      }
    }
    Quiz saveThisQuiz = quizRepository.saveAndFlush(quizToCreate);
    return quizMapper.entityToDto(saveThisQuiz);
  }

  @Override
  public QuizResponseDto deleteQuiz(Long id){
    Quiz quizToDelete = quizRepository.getById(id);
    quizRepository.delete(quizToDelete);
    return quizMapper.entityToDto(quizToDelete);
  }

  @Override
  public QuizResponseDto renameQuiz(Long id, String newName){
    Quiz quizToRename = quizRepository.getById(id);
    quizToRename.setName(newName);
    quizRepository.saveAndFlush(quizToRename);
    return quizMapper.entityToDto(quizToRename);
  }
  @Override
  public QuestionResponseDto randomQuestion(Long id){
    List<Question> questions = quizRepository.getById(id).getQuestions();
    Random random = new Random();
    Question randomQuestion = questions.get(random.nextInt(questions.size()));
    return questionMapper.entityToDto(randomQuestion);

  }

  @Override
  public ResponseEntity<QuizResponseDto> modifiedQuestion(Long id, QuestionRequestDto questionRequestDto){
    Optional<Quiz> optionalQuiz = quizRepository.findById(id);
    if (optionalQuiz.isEmpty()){
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Quiz quizToUpdate = optionalQuiz.get();
    Question questionToAdd = questionMapper.requestDtoToEntity(questionRequestDto);
    questionRepository.saveAndFlush(questionToAdd).setQuiz(quizToUpdate);
    for (Answer answer : questionToAdd.getAnswers()){
      answer.setQuestion(questionToAdd);
      answerRepository.saveAndFlush(answer);
    }
      return new ResponseEntity<>(quizMapper.entityToDto(quizToUpdate), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<QuestionResponseDto> deletedQuestion(Long id, Long questionID){
    Optional<Quiz> optionalQuiz = quizRepository.findById(id);
    Optional<Question> optionalQuestion = questionRepository.findById(questionID);
    if (optionalQuiz.isEmpty() || optionalQuestion.isEmpty()){
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    Question questionToDelete = optionalQuestion.get();
    for (Answer answer : questionToDelete.getAnswers()){
      answerRepository.delete(answer);
    }
    questionRepository.delete(questionToDelete);
    return new ResponseEntity<>(questionMapper.entityToDto(questionToDelete), HttpStatus.OK);
  }

}
