package ru.yandex.practicum.filmorate.sevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataDoesNotExistsException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.LikeReviewStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class ReviewService extends ModelService<Review, ReviewStorage> {

    FilmService filmService;
    UserService userService;
    LikeReviewStorage likeReviewStorage;

    public ReviewService(ReviewStorage reviewStorage, FilmService filmService,
                         UserService userService, LikeReviewStorage likeReviewStorage) {
        this.storage = reviewStorage;
        this.filmService = filmService;
        this.userService = userService;
        this.likeReviewStorage = likeReviewStorage;
    }

    public Collection<Review> getByFilmId(Optional<Long> id, int count) {
        Collection<Review> col;
        if(id.isPresent()) {
            filmService.getElement(id.get());
            col = storage.findByFilmId(id.get(), count);
        } else {
            col = storage.getAll();
        }
        return col;
    }

    @Override
    public Review putElement(Review element) {
        if(element.getId() != null) {
            Optional<Review> gotted = storage.getElement(element.getId());
            gotted.ifPresent(value -> {
                element.setUseful(value.getUseful());
                element.setUserId(value.getUserId());
                element.setFilmId(value.getFilmId());
            });
            return super.putElement(element);
        }
        return super.putElement(element);
    }

    public void putLikeReview(long reviewId, long userId) {
        Review review = getElement(reviewId);
        userService.getElement(userId);

        Optional<Boolean> status = likeReviewStorage.getStatus(reviewId, userId);
        status.ifPresentOrElse((value) -> {
            if (!value) {
                likeReviewStorage.update(reviewId, userId, true);
                review.setUseful(review.getUseful() + 2);
            }
        }, () -> {
            likeReviewStorage.put(reviewId, userId, true);
            review.setUseful(review.getUseful() + 1);
        });

        storage.update(review);
    }

    public void putDislikeReview(long reviewId, long userId) {
        Review review = getElement(reviewId);
        userService.getElement(userId);

        Optional<Boolean> status = likeReviewStorage.getStatus(reviewId, userId);
        status.ifPresentOrElse((value) -> {
            if (value) {
                likeReviewStorage.update(reviewId, userId, false);
                review.setUseful(review.getUseful() - 2);
            }
        }, () -> {
            likeReviewStorage.put(reviewId, userId, false);
            review.setUseful(review.getUseful() - 1);
        });

        storage.update(review);
    }

    public void deleteLikeReview(long reviewId, long userId) {
        Review review = getElement(reviewId);
        userService.getElement(userId);

        Optional<Boolean> status = likeReviewStorage.getStatus(reviewId, userId);
        if (status.orElseThrow(() -> new DataDoesNotExistsException("Лайк не существует"))) {
            likeReviewStorage.delete(reviewId, userId);
            review.setUseful(review.getUseful() - 1);
        }

        storage.update(review);
    }

    public void deleteDislikeReview(long reviewId, long userId) {
        Review review = getElement(reviewId);
        userService.getElement(userId);

        Optional<Boolean> status = likeReviewStorage.getStatus(reviewId, userId);
        if (status.orElseThrow(() -> new DataDoesNotExistsException("Дизлайк не существует"))) {
            likeReviewStorage.delete(reviewId, userId);
            review.setUseful(review.getUseful() + 1);
        }

        storage.update(review);
    }





    @Override
    protected boolean isValid(Review element) {
       userService.getElement(element.getUserId());
       filmService.getElement(element.getFilmId());
        return true;
    }
}
