package com.ecomm.productService.service;

import com.ecomm.productService.entity.Product;
import com.ecomm.productService.exception.ProductServiceCustomException;
import com.ecomm.productService.model.ProductRequest;
import com.ecomm.productService.model.ProductResponse;
import com.ecomm.productService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product ...... ");

        Product product
                = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("product created");
        return product.getProductID();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Get the product for the productId : {} " , productId);
        Product product
                = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException("Product with given Id not found" , "PRODUCT NOT_FOUND"));

        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product , productResponse);
        return null;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce Quantity {} for Id: {}" , quantity , productId);

        Product product
                = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException(
                        "Product with given Id not found" , "PRODUCT_NOT_FOUND"
                ));
        if(product.getQuantity() < quantity){
            throw new ProductServiceCustomException("Product doesnt have sufficient quantity",
                    "INSUFFICIENT_QUANTITY");
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        log.info("Product quantity Updated successfully");
    }
}
