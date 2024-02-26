package com.ecomm.productService.service;

import com.ecomm.productService.model.ProductRequest;
import com.ecomm.productService.model.ProductResponse;

public interface ProductService {

    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
