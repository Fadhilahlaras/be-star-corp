package coba.star.corp.controller.restapi;

import coba.star.corp.model.dto.ProductDto;
import coba.star.corp.model.entity.Product;
import coba.star.corp.model.entity.ProductCategory;
import coba.star.corp.repository.ProductRepository;
import coba.star.corp.service.ProductCategoryService;
import coba.star.corp.service.ProductService;

import org.jfree.util.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Base64;

@RestController
@RequestMapping("/api/product")
public class ApiProduct {

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getListProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductDto> productDtos = productList.stream().map(product -> mapProductToProductDto(product))
                .collect(Collectors.toList());
        // List<ProductDto> body = productService.listProducts();
        return new ResponseEntity<List<ProductDto>>(productDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Integer id) {
        Product product = productRepository.findById(id).get();
        ProductDto productDto = new ProductDto();
        modelMapper.map(product, productDto);
        modelMapper.map(product.getProductCategory(), productDto);
        productDto.setId(product.getId());
        return new ResponseEntity<ProductDto>(productDto, HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductDto>> getProducts(@PathVariable Integer id) {
        List<ProductDto> body = listProductByCategory(id);
        return new ResponseEntity<List<ProductDto>>(body, HttpStatus.OK);
    }

    public List<ProductDto> listProductByCategory(Integer id) {
        List<Product> products = productRepository.cariProductCategory(id);
        List<ProductDto> productDtos = products.stream().map(product -> mapProductToProductDto(product))
                .collect(Collectors.toList());

        return productDtos;
    }

    @GetMapping("/find/{product}")
    public ResponseEntity<List<ProductDto>> getProducts(@PathVariable String product) {
        // String search= "\\y" +product+"\\y";
        List<ProductDto> body = searchProduct(product);
        return new ResponseEntity<List<ProductDto>>(body, HttpStatus.OK);
    }

    public List<ProductDto> searchProduct(String search) {
        List<Product> products = productRepository.searchProduct(search);
        List<ProductDto> productDtos = products.stream().map(product -> mapProductToProductDto(product))
                .collect(Collectors.toList());

        return productDtos;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/save")
    public ProductDto editSaveProduct(@RequestPart(value = "data", required = true) ProductDto productDto,
            @RequestPart(value = "pictureUrl", required = false) MultipartFile file) throws Exception {
        Product product = modelMapper.map(productDto, Product.class);

        if (file == null) {
            product.setPictureUrl(productRepository.findById(productDto.getId()).get().getPictureUrl());
        } else {
            String userFolderPath = "D:/Laras/upload/";
            Path path = Paths.get(userFolderPath);
            Path filePath = path.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            product.setPictureUrl(file.getOriginalFilename());
        }

        product.setIdCategory(productDto.getIdCategory());
        product = productService.saveProductMaterDetail(product);
        ProductDto productDtoDB = mapProductToProductDto(product);

        return productDtoDB;
    }

    private ProductDto mapProductToProductDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productDto.setIdCategory(product.getProductCategory().getId());
        productDto.setCategoryName(product.getProductCategory().getCategoryName());
        productDto.setId(product.getId());
        return productDto;
    }
    //
    // @PostMapping("/add")
    // public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto
    // productDto) {
    // Optional<ProductCategory> optionalProductCategory =
    // productCategoryService.readProductCategory(productDto.getIdCategory());
    // if (!optionalProductCategory.isPresent()) {
    // return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category is
    // invalid"), HttpStatus.CONFLICT);
    // }
    // ProductCategory productCategory = optionalProductCategory.get();
    // productService.addProduct(productDto, productCategory);
    // return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Product has
    // been added"), HttpStatus.CREATED);
    // }

    // @PostMapping("/update/{IDproduct}")
    // public ResponseEntity<ApiResponse> updateProduct(@PathVariable("IDproduct")
    // int idProduct, @RequestBody @Valid ProductDto productDto) {
    // Optional<ProductCategory> optionalProductCategory =
    // productCategoryService.readProductCategory(productDto.getIdCategory());
    // if (!optionalProductCategory.isPresent()) {
    // return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category is
    // invalid"), HttpStatus.CONFLICT);
    // }
    // ProductCategory productCategory = optionalProductCategory.get();
    // productService.updateProduct(idProduct, productDto, productCategory);
    // return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Product has
    // been updated"), HttpStatus.OK);
    // }

    @GetMapping("/getImage/{id}")
    public String getImage(@PathVariable Integer id) throws IOException {
        Log.debug("masuk sini");
        Product product = productRepository.findById(id).get();
        String userFolderPath = "D:/Star/upload/";
        Log.debug(userFolderPath);
        String pathFile = userFolderPath + product.getPictureUrl();
        Log.debug(pathFile);
        Path paths = Paths.get(pathFile);
        byte[] foto = Files.readAllBytes(paths);
        String img = Base64.getEncoder().encodeToString(foto);
        return img;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        productRepository.deleteById(id);
    }

}
