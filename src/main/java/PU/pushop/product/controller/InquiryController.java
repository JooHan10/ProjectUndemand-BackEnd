package PU.pushop.product.controller;

import PU.pushop.members.entity.Member;
import PU.pushop.product.entity.Inquiry;
import PU.pushop.product.entity.Product;
import PU.pushop.product.entity.enums.InquiryType;
import PU.pushop.product.model.InquiryDto;
import PU.pushop.product.service.InquiryService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inquiry")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    // Request Data
    @Data
    static class InquiryRequest {
        private Long productId;
        private InquiryType inquiryType;
        private String inquiryTitle;
        private String inquiryContent;
        private String password;
        private Boolean isSecret;
    }

    private Inquiry InquiryFormRequest(InquiryRequest request) {
        Inquiry inquiry = new Inquiry();

        inquiry.setInquiryType(request.getInquiryType());
        inquiry.setInquiryTitle(request.getInquiryTitle());
        inquiry.setInquiryContent(request.getInquiryContent());
        inquiry.setPassword(request.getPassword());
        inquiry.setIsSecret(request.getIsSecret());

        return inquiry;
    }

    /**
     * 전체 문의글 보기
     * @return
     */
    @GetMapping("")
    public List<InquiryDto> getAllInquiries() {
        return inquiryService.allInquiryList();
    }

    /**
     * 특정 상품 문의글 리스트
     * @param productId
     * @return
     */
    @GetMapping("/list/{productId}")
    public List<InquiryDto> getProductInquiries(@PathVariable Long productId) {
        return inquiryService.inquiryListByProductId(productId);
    }


    /**
     * 문의글 작성
     * @param request
     * @param memberId
     * @param productId
     * @return
     */
    @PostMapping("/new/{productId}/{memberId}")
    public ResponseEntity<?> createInquiry(@Valid @RequestBody InquiryRequest request, @PathVariable Long memberId, @PathVariable Long productId) {
        Inquiry inquiry = InquiryFormRequest(request);
        Long createdId = inquiryService.createInquiry(inquiry, memberId,productId);
        return ResponseEntity.ok(createdId);
    }

    /**
     * 문의글 상세보기
     * @param inquiryId
     * @return
     */
    @GetMapping("/{inquiryId}")
    public ResponseEntity<?> getInquiryById(@PathVariable Long inquiryId,@RequestParam(required = false) String password) {
        Inquiry inquiryDetail = inquiryService.inquiryDetail(inquiryId);

    /*    // 비밀글인 경우에만 비밀번호 검증
        if (inquiryDetail.getIsSecret()) {
            if (password == null || !inquiryDetail.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호를 입력해야 합니다.");
            }
        }*/

        InquiryDto inquiryDto = new InquiryDto(inquiryDetail);
        return new ResponseEntity<>(inquiryDto, HttpStatus.OK);
    }

    /**
     * 문의글 수정
     * @param inquiryId
     * @param request
     * @return
     */
    @PutMapping("/{inquiryId}")
    public ResponseEntity<?> updateInquiry(@PathVariable Long inquiryId, @Valid @RequestBody InquiryRequest request, @RequestParam String password) {
        Inquiry updatedInquiry = InquiryFormRequest(request);
        Inquiry updated = inquiryService.updateInquiry(inquiryId, updatedInquiry, password);

        return ResponseEntity.ok(updated);
    }

    /**
     * 문의글 삭제
     * @param inquiryId
     * @return
     */
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<?> deleteInquiry(@PathVariable Long inquiryId, @RequestParam String password) {
        inquiryService.deleteInquiry(inquiryId,password);
        return ResponseEntity.ok().build();
    }


}
