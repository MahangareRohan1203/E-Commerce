package com.flipkart.ecommerce.request;

import com.flipkart.ecommerce.model.Size;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @Min(1)
    private Integer price;
    @Min(1)
    private Integer discountedPrice;
    @Min(0)
    private Integer discountedPercent;
    @Min(0)
    private Integer quantity;
    @NotBlank
    private String brand;
    @NotBlank
    private String color;

    private Set<Size> size = new HashSet<>();
    @NotBlank
    private String imageUrl;
    @NotBlank
    private String topLevelCategory;
    @NotBlank
    private String secondLevelCategory;
    @NotBlank
    private String thirdLevelCategory;
}
