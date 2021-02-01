package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Repository for Answer entity
 */
@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Creates new answer entity in database
     *
     * @param answerEntity entity to be persisted in database.
     * @return persisted entity
     */
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    /**
     * Gets an answer from the database with uuid
     *
     * @param answerUuid - uuid of answer
     * @return answer entity of uuid
     */
    public AnswerEntity getAnswerByUuid(final String answerUuid) {
        try {
            return entityManager.createNamedQuery("answerEntityByUuid", AnswerEntity.class).setParameter("uuid", answerUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Deletes an answer entity from database
     *
     * @param answerId - answer id to be deleted
     */
    public void userAnswerDelete(final String answerId) {
        AnswerEntity answerEntity = getAnswerByUuid(answerId);
        entityManager.remove(answerEntity);
    }

    /**
     * Gets all answers for the question
     *
     * @param questionId - question id to get all answers
     * @return list of answer entities of the question
     */
    public List<AnswerEntity> getAllAnswersToQuestion(final String questionId) {
        try {
            return entityManager.createNamedQuery("answerEntityByQuestionId", AnswerEntity.class).setParameter("uuid", questionId).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Updates an answer entity in database
     *
     * @param answerEntity - answer entity to be updated
     * @return updated entity
     */
    public AnswerEntity editAnswerContent(final AnswerEntity answerEntity) {
        return entityManager.merge(answerEntity);
    }
}