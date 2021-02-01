package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * AnswerController - RestController for handling all the answers to the question in quora applications.
 */
@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    /**
     * createAnswer - creates new answer for the question
     *
     * @param answerRequest - request model for new answer of a question
     * @param questionId - question id for which the answer is posted
     * @param authorization - authorization token of user
     * @return response entity of answer response
     * @throws AuthorizationFailedException throws when authorization failed
     * @throws InvalidQuestionException throws when question is not available
     */
    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(final AnswerRequest answerRequest, @PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {

        // Create answer entity
        final AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAnswer(answerRequest.getAnswer());

        // Return response with created answer entity
        final AnswerEntity createdAnswerEntity = answerService.createAnswer(answerEntity, questionId, authorization);
        AnswerResponse answerResponse = new AnswerResponse().id(createdAnswerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    /**
     * editAnswerContent - edits answer content of the question
     *
     * @param answerEditRequest - request model existing answer of a question
     * @param answerId - question id for which the answer is edited.
     * @param authorization - authorization token of user
     * @return response entity of answer edit response
     * @throws AuthorizationFailedException throws when authorization failed
     * @throws AnswerNotFoundException throws when answer is not available
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(final AnswerEditRequest answerEditRequest, @PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {

        // Created answer entity for further update
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAnswer(answerEditRequest.getContent());
        answerEntity.setUuid(answerId);

        // Return response with updated answer entity
        AnswerEntity updatedAnswerEntity = answerService.editAnswerContent(answerEntity, authorization);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(updatedAnswerEntity.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }

    /**
     * deleteAnswer - deletes the answer for the question
     *
     * @param answerId - answer id to be deleted
     * @param authorization - authorization token of user
     * @return response entity of answer delete response
     * @throws AuthorizationFailedException throws when authorization failed
     * @throws AnswerNotFoundException throws when answer is not available
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {

        // Delete requested answer
        answerService.deleteAnswer(answerId, authorization);

        // Return response
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(answerId).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }

    /**
     * getAllAnswersToQuestion - gets all answer to the given question id.
     *
     * @param questionId - question id for which answers should be retrieved
     * @param authorization - authorization token of user
     * @return response entity of list of answer details response
     * @throws AuthorizationFailedException throws when authorization failed
     * @throws InvalidQuestionException throws when question is not available
     */
    @RequestMapping(method = RequestMethod.GET, path = "/answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion (@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {

        // Get all answers for requested question
        List<AnswerEntity> allAnswers = answerService.getAllAnswersToQuestion(questionId, authorization);

        // Create response
        List<AnswerDetailsResponse> allAnswersResponse = new ArrayList<AnswerDetailsResponse>();

        for (int i = 0; i < allAnswers.size(); i++) {
            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse()
                    .answerContent(allAnswers.get(i).getAnswer())
                    .questionContent(allAnswers.get(i).getQuestion().getContent())
                    .id(allAnswers.get(i).getUuid());
            allAnswersResponse.add(answerDetailsResponse);
        }

        // Return response
        return new ResponseEntity<List<AnswerDetailsResponse>>(allAnswersResponse, HttpStatus.FOUND);
    }
}