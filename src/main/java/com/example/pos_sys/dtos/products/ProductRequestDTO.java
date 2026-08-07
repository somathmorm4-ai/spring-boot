package com.example.pos_sys.dtos.products;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;


@Data
public class ProductRequestDTO {

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must not exceed 100 characters")
    private String product_name;

    @NotNull(message = "Category id is required")
    private Long categry_id;

    @NotNull(message = "Price is Required")
    @Digits(integer = 10, fraction = 2)
    @DecimalMin(value = "0.0", inclusive = false)




    private double price;


}
