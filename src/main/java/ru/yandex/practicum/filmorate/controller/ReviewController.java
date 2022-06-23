package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.sevice.ReviewService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController extends DefaultController<ReviewService, Review> {

    @Autowired
    public ReviewController(ReviewService service) {
        super(service);
    }


    @GetMapping(params = {"filmId"})
    public Collection<Review> getByFilmId(@RequestParam(defaultValue = "10", required = false) Integer count,
                                              @RequestParam(required = false) Long filmId) {
        return service.getByFilmId(Optional.ofNullable(filmId), count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLikeReview(@PathVariable("id") long reviewId, @PathVariable("userId") long userId) {
        service.putLikeReview(reviewId, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void putDislikeReview(@PathVariable("id") long reviewId, @PathVariable("userId") long userId) {
        service.putDislikeReview(reviewId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeReview(@PathVariable("id") long reviewId, @PathVariable("userId") long userId) {
        service.deleteLikeReview(reviewId, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislikeReview(@PathVariable("id") long reviewId, @PathVariable("userId") long userId) {
        service.deleteDislikeReview(reviewId, userId);
    }

}
