package com.product.productreviewanalysis.dto.product;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDtoToEntity {

    @NotBlank
    @NotNull(message = "Name can't Be Null")
    private String name;
    @NotNull
    private int price;
    @NotNull
    private int quantity;
    @NotNull(message = "You Should Add it in any category !")
    @NotBlank
    private String category;
    private String feedbacks;
    @Min(1)
    @Max(5)
    private int rate;
}
