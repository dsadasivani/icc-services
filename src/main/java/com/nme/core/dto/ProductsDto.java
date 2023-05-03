package com.nme.core.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductsDto {
    private String productSelected;
    private String quantity;
    private String unitPrice;
    private String activeFlag;
}
