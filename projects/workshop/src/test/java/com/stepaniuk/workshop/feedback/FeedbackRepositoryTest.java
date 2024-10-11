package com.stepaniuk.workshop.feedback;

import com.stepaniuk.workshop.testspecific.JpaLevelTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JpaLevelTest
@Sql(scripts = "classpath:sql/feedbacks.sql")
public class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Test
    void shouldSaveFeedback() {
        Feedback feedback = new Feedback(
                null,
                1L,
                1L,
                5,
                "Great service!",
                null,
                null,
                List.of(1L, 2L)
        );

        Feedback savedFeedback = feedbackRepository.save(feedback);

        assertNotNull(savedFeedback);
        assertNotNull(savedFeedback.getId());

        assertEquals(feedback.getOrderId(), savedFeedback.getOrderId());
        assertEquals(feedback.getReviewerId(), savedFeedback.getReviewerId());
        assertEquals(feedback.getRating(), savedFeedback.getRating());
        assertEquals(feedback.getComment(), savedFeedback.getComment());
        assertEquals(feedback.getCategories().get(0), savedFeedback.getCategories().get(0));
    }

    @Test
    void shouldThrowExceptionWhenSavingFeedback(){
        Feedback feedback = new Feedback(
                null,
                1L,
                1L,
                null,
                "Great service!",
                null,
                null,
                List.of(1L, 2L)
        );

        assertThrows(DataIntegrityViolationException.class, () -> feedbackRepository.save(feedback));
    }

    @Test
    void shouldFindFeedbackById() {
        Optional<Feedback> feedback = feedbackRepository.findById(1L);
        assertTrue(feedback.isPresent());

        Feedback foundFeedback = feedback.get();

        assertEquals(1L, foundFeedback.getId());
        assertEquals(1L, foundFeedback.getOrderId());
        assertEquals(1L, foundFeedback.getReviewerId());
        assertEquals(5, foundFeedback.getRating());
        assertEquals("Great service!", foundFeedback.getComment());
        assertEquals(Instant.parse("2021-01-01T00:22:09.266615Z"), foundFeedback.getCreatedAt());
        assertEquals(Instant.parse("2021-01-01T00:22:09.266615Z"), foundFeedback.getLastModifiedAt());
        assertEquals(List.of(1L), foundFeedback.getCategories());
    }

    @Test
    void shouldUpdateFeedback(){
        Optional<Feedback> feedback = feedbackRepository.findById(1L);
        assertTrue(feedback.isPresent());

        Feedback foundFeedback = feedback.get();

        foundFeedback.setComment("new comment");

        Feedback savedFeedback = feedbackRepository.save(foundFeedback);

        assertNotNull(savedFeedback);
        assertEquals(savedFeedback.getComment(), foundFeedback.getComment());
    }

    @Test
    void shouldDeleteFeedbackWhenDeletingExistingFeedback(){
        Optional<Feedback> feedback = feedbackRepository.findById(1L);
        assertTrue(feedback.isPresent());

        feedbackRepository.delete(feedback.get());

        assertTrue(feedbackRepository.findById(1L).isEmpty());
    }

    @Test
    void shouldDeleteFeedbackWhenDeletingFeedbackById(){
        feedbackRepository.deleteById(1L);
        assertTrue(feedbackRepository.findById(1L).isEmpty());
    }

    @Test
    void shouldReturnNonEmptyListOfFeedbacks(){
        List<Feedback> feedbacks = feedbackRepository.findAll();

        assertNotNull(feedbacks);
        assertFalse(feedbacks.isEmpty());
    }

    @Test
    void shouldReturnNonEmptyPageOfFeedbacks(){
        Page<Feedback> feedbacks = feedbackRepository.findAll(
                PageRequest.of(0,2)
        );

        assertNotNull(feedbacks);
        assertFalse(feedbacks.isEmpty());

        List<Feedback> feedbackList = feedbacks.getContent();
        assertNotNull(feedbackList);
        assertFalse(feedbackList.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 5})
    void shouldReturnListOfFeedbacksWhereRatingIsEqualToValue(int rating){

        Specification<Feedback> feedbackSpecification = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("rating"), rating));

        List<Feedback> feedbacks = feedbackRepository.findAll(feedbackSpecification);

        assertNotNull(feedbacks);
        assertFalse(feedbacks.isEmpty());

        assertAll(
                feedbacks.stream().map(
                       feedback -> () -> assertEquals(rating, feedback.getRating())
                )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"Great service!","Normal service!"})
    void shouldReturnNonEmptyPageOfFeedbacksWhereCommentIsEqualsToParam(String comment){
        Specification<Feedback> feedbackSpecification = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("comment"), comment));

        Page<Feedback> feedbacks = feedbackRepository.findAll(feedbackSpecification, PageRequest.of(0,2));

        assertNotNull(feedbacks);
        assertFalse(feedbacks.isEmpty());

        List<Feedback> feedbackList = feedbacks.getContent();
        assertNotNull(feedbackList);
        assertFalse(feedbackList.isEmpty());

        assertAll(
                feedbackList.stream().map(
                        feedback -> () -> assertEquals(comment, feedback.getComment())
                )
        );
    }
}
