package org.alilopez.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.alilopez.model.Review;
import org.alilopez.service.ReviewService;

public class ReviewController {
    private ReviewService reviewService;
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public void createReview(Context ctx){
        try {
            Review review = ctx.bodyAsClass(Review.class);
            reviewService.createReview(review);
            ctx.status(HttpStatus.CREATED).json(review);
        } catch (Exception e) {
            ctx.status(HttpStatus.BAD_REQUEST).json(e.getMessage());
        }
    }
}
