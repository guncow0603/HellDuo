package com.hellduo.domain.review.service;

import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.pt.entity.enums.PTStatus;
import com.hellduo.domain.pt.repository.PTRepository;
import com.hellduo.domain.review.dto.request.ReviewCreateReq;
import com.hellduo.domain.review.dto.response.GetReviewRes;
import com.hellduo.domain.review.dto.response.GetReviewsRes;
import com.hellduo.domain.review.dto.response.ReviewCreateRes;
import com.hellduo.domain.review.entity.Review;
import com.hellduo.domain.review.repository.ReviewRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PTRepository ptRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User user;
    private PT pt;
    private ReviewCreateReq reviewCreateReq;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .role(UserRoleType.USER)
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        User trainer = User.builder()
                .role(UserRoleType.USER)
                .build();
        ReflectionTestUtils.setField(trainer, "id", 1L);

        pt = PT.builder()
                .status(PTStatus.COMPLETED)
                .trainer(trainer)
                .build();
        ReflectionTestUtils.setField(pt, "id", 1L);

        reviewCreateReq = new ReviewCreateReq("제목", "내용", 4.5);
    }

    @Test
    @DisplayName("후기 작성 성공 테스트")
    void reviewCreate_Success() {
        // Given
        when(reviewRepository.findByPtId(anyLong())).thenReturn(null);
        when(ptRepository.findPTByIdWithThrow(anyLong())).thenReturn(pt);

        // When
        ReviewCreateRes response = reviewService.reviewCreate(reviewCreateReq, user, 1L);

        // Then
        assertNotNull(response);
        assertEquals("후기 작성 완료.", response.msg());
        verify(reviewRepository).save(any(Review.class)); // 수정: ArgumentMatchers.any -> any()
    }

    @Test
    @DisplayName("트레이너 리뷰 조회 성공")
    void getTrainerReviews_Success() {
        // Given
        Review review1 = Review.builder()
                .title("리뷰1")
                .pt(pt)
                .trainer(pt.getTrainer())
                .rating(4.5)
                .build();
        ReflectionTestUtils.setField(review1, "id", 1L);

        Review review2 = Review.builder()
                .title("리뷰2")
                .pt(pt)
                .trainer(pt.getTrainer())
                .rating(3.0)
                .build();
        ReflectionTestUtils.setField(review2, "id", 2L);  // id 설정 변경

        List<Review> reviews = List.of(review1, review2);
        when(reviewRepository.findAllByTrainerId(anyLong())).thenReturn(reviews);

        // When
        List<GetReviewsRes> response = reviewService.getTrainerReviews(2L);

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(4.5, response.get(0).rating());
        assertEquals(3.0, response.get(1).rating());
    }

    @Test
    @DisplayName("후기 상세 조회 성공")
    void getReview_Success() {
        // Given
        Review review = Review.builder()
                .title("상세리뷰")
                .content("리뷰 내용")
                .rating(5.0)
                .build();
        ReflectionTestUtils.setField(review, "id", 1L);

        when(reviewRepository.findReviewByIdWithThrow(anyLong())).thenReturn(review);

        // When
        GetReviewRes response = reviewService.getReview(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.reviewId());
        assertEquals("상세리뷰", response.title());
        assertEquals("리뷰 내용", response.content());
        assertEquals(5.0, response.rating());
    }
}