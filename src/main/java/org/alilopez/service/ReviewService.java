package org.alilopez.service;

import io.javalin.Javalin;
import org.alilopez.model.Review;
import org.alilopez.repository.ReviewRepository;

import java.sql.SQLException;

public class ReviewService {
    private ReviewRepository reviewRepository;
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void createReview(Review review) throws SQLException {
        reviewRepository.save(review);
    }

}
