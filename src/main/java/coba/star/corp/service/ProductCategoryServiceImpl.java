package coba.star.corp.service;

import coba.star.corp.model.entity.ProductCategory;
import coba.star.corp.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategory saveProductCategoryMaterDetail(ProductCategory productCategory) {
        productCategory = productCategoryRepository.save(productCategory);
        return productCategory;
    }
}
