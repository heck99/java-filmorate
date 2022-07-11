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
        log.info("id = {}, count = {}", id, count);
        Collection<Review> col;
        if(id.isPresent()) {
            log.info("Обращаемся к хранилищу. Поиск по filmId");
            filmService.getElement(id.get());
            col = storage.findByFilmId(id.get(), count);
        } else {
            log.info("Обращаемся к хранилищу. Поиск по всем фильмам");
            col = storage.getAll();
        }
        return col;
    }

    @Override
    public Review putElement(Review element) {
        log.info("element.id = {}", element.getId());
        if(element.getId() != null) {
            log.info("Обновляем поля элемента");
            Review gotted = getElement(element.getId());
            element.setUseful(gotted.getUseful());
            element.setUserId(gotted.getUserId());
            element.setFilmId(gotted.getFilmId());
            return super.putElement(element);
        }
        log.info("Сщздаём новый элемент");
        return super.putElement(element);
    }

    public void putLikeReview(long reviewId, long userId) {
        log.info("Проверяем наличие отзыва с id = {}", reviewId);
        Review review = getElement(reviewId);
        log.info("Проверяем наличие пользователя с id = {}", userId);
        userService.getElement(userId);
        Optional<Boolean> status = likeReviewStorage.getStatus(reviewId, userId);
        status.ifPresentOrElse((value) -> {
            if (!value) {
                log.info("Обновляем дизлайк на лайк");
                likeReviewStorage.update(reviewId, userId, true);
                review.setUseful(review.getUseful() + 2);
            }
        }, () -> {
            log.info("Ставим лайк");
            likeReviewStorage.put(reviewId, userId, true);
            review.setUseful(review.getUseful() + 1);
        });
        log.info("Обновляем отзыв");
        storage.update(review);
    }

    public void putDislikeReview(long reviewId, long userId) {
        log.info("Проверяем наличие отзыва с id = {}", reviewId);
        Review review = getElement(reviewId);
        log.info("Проверяем наличие пользователя с id = {}", userId);
        userService.getElement(userId);
        Optional<Boolean> status = likeReviewStorage.getStatus(reviewId, userId);
        status.ifPresentOrElse((value) -> {
            if (value) {
                log.info("Обновляем лайк на дизлайк");
                likeReviewStorage.update(reviewId, userId, false);
                review.setUseful(review.getUseful() - 2);
            }
        }, () -> {
            log.info("Ставим дизлайк");
            likeReviewStorage.put(reviewId, userId, false);
            review.setUseful(review.getUseful() - 1);
        });

        log.info("Обновляем отзыв");
        storage.update(review);
    }

    public void deleteLikeReview(long reviewId, long userId) {
        log.info("Проверяем наличие отзыва с id = {}", reviewId);
        Review review = getElement(reviewId);
        log.info("Проверяем наличие пользователя с id = {}", userId);
        userService.getElement(userId);

        Optional<Boolean> status = likeReviewStorage.getStatus(reviewId, userId);
        if (status.orElseThrow(() -> new DataDoesNotExistsException("Лайк не существует"))) {
            log.info("Удаляем лайк");
            likeReviewStorage.delete(reviewId, userId);
            review.setUseful(review.getUseful() - 1);
        }
        log.info("Обновляем отзыв");
        storage.update(review);
    }

    public void deleteDislikeReview(long reviewId, long userId) {
        log.info("Проверяем наличие отзыва с id = {}", reviewId);
        Review review = getElement(reviewId);
        log.info("Проверяем наличие пользователя с id = {}", userId);
        userService.getElement(userId);

        Optional<Boolean> status = likeReviewStorage.getStatus(reviewId, userId);
        if (status.orElseThrow(() -> new DataDoesNotExistsException("Дизлайк не существует"))) {
            log.info("Удаляем дизлайк");
            likeReviewStorage.delete(reviewId, userId);
            review.setUseful(review.getUseful() + 1);
        }
        log.info("Обновляем отзыв");
        storage.update(review);
    }

    @Override
    protected boolean isValid(Review element) {
       userService.getElement(element.getUserId());
       filmService.getElement(element.getFilmId());
        return true;
    }
}
