package coba.star.corp.service;

import coba.star.corp.model.entity.Product;
import coba.star.corp.repository.ProductCategoryRepository;
import coba.star.corp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public Product saveProductMaterDetail(Product product) {
        product = productRepository.save(product);
        product.setProductCategory(productCategoryRepository.findById(product.getIdCategory()).get());
        return product;
    }
}
