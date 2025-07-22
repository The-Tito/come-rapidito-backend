package org.alilopez.routes;

import io.javalin.Javalin;
import org.alilopez.controller.ReviewController;

public class ReviewRoutes {
    private ReviewController reviewController;
    public ReviewRoutes(ReviewController reviewController) {
        this.reviewController = reviewController;
    }

    public void Reviews(Javalin app) {
        app.post("/api/reviews", reviewController::createReview);
    }
}
