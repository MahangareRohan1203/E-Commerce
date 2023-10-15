package com.flipkart.ecommerce.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long productId;
    private String review;
}
