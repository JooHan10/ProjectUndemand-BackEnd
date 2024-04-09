package PU.pushop.review.model;

import PU.pushop.Inquiry.model.InquiryReplyDto;
import PU.pushop.payment.entity.PaymentHistory;
import PU.pushop.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long reviewId;
    private Long paymentHistoryId;
    private String reviewContent;
    private int rating;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private Long memberId;
    private String writer;
    private Long productId;
    private String productName;
    private List<ReviewReplyDto> replies;

    public ReviewDto(Review review) {
        this(
                review.getReviewId(),
                review.getPaymentHistory().getId(),
                review.getReviewContent(),
                review.getRating(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                review.getPaymentHistory().getOrders().getMember().getId(),
                review.getPaymentHistory().getOrders().getMember().getUsername(),
                review.getPaymentHistory().getProduct().getProductId(),
                review.getPaymentHistory().getProduct().getProductName(),
                review.getReplies() != null ? review.getReplies().stream().map(ReviewReplyDto::new).collect(Collectors.toList()) : null
        );
    }

    public static Review requestForm(ReviewDto request) {
        Review review = new Review();

        review.setReviewContent(request.getReviewContent());
        review.setRating(request.getRating());

        return review;
    }

}
