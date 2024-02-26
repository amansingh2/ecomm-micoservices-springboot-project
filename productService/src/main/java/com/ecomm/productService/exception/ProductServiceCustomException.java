package com.ecomm.productService.exception;

import com.ecomm.productService.service.ProductService;
import lombok.Data;

@Data
public class ProductServiceCustomException extends RuntimeException{
    private String errorCode;
    public ProductServiceCustomException(String message , String erroeCode){
        super(message);
        this.errorCode = erroeCode;
    }
}
