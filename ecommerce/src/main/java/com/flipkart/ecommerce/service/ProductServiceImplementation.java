package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.ProductException;
import com.flipkart.ecommerce.model.Category;
import com.flipkart.ecommerce.model.Product;
import com.flipkart.ecommerce.repository.CategoryRepository;
import com.flipkart.ecommerce.repository.ProductRepository;
import com.flipkart.ecommerce.request.CreateProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest productRequest) {
        Category topLevel = categoryRepository.findByName(productRequest.getTopLevelCategory());
        if (topLevel == null) {
            Category topLevelCategory = new Category();
            topLevelCategory.setName(productRequest.getTopLevelCategory());
            topLevelCategory.setLevel(1);
            topLevel = categoryRepository.save(topLevelCategory);
        }

        Category secondLevel = categoryRepository.findByNameAndParent(productRequest.getSecondLevelCategory(), topLevel.getName());
        if (secondLevel == null) {
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(productRequest.getSecondLevelCategory());
            secondLevelCategory.setParentCategory(topLevel);
            secondLevelCategory.setLevel(2);
            secondLevel = categoryRepository.save(secondLevelCategory);
        }

        Category thirdLevel = categoryRepository.findByNameAndParent(productRequest.getThirdLevelCategory(), secondLevel.getName());
        if (thirdLevel == null) {
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(productRequest.getThirdLevelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);
            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        Product product = new Product();
        product.setTitle(productRequest.getTitle());
        product.setColor(productRequest.getColor());
        product.setDescription(productRequest.getDescription());
        product.setDiscountedPrice(productRequest.getDiscountedPrice());
        product.setDiscountedPercent(productRequest.getDiscountedPercent());
        product.setImageUrl(productRequest.getImageUrl());
        product.setBrand(productRequest.getBrand());
        product.setPrice(productRequest.getPrice());
        product.setSizes(productRequest.getSize());
        //TODO: PENDING WORK quantity pending
        product.setQuantity(productRequest.getQuantity());
        product.setCategory(thirdLevel);


        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        product.getSizes().clear();
        productRepository.delete(product);
        return "Product Deleted Successfully ";
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        Product existingProduct = findProductById(productId);
        if (product.getTitle() != null) {
            existingProduct.setTitle(product.getTitle());
        }
        if(product.getDescription() != null) existingProduct.setDescription(product.getDescription());
        if(product.getColor() != null) existingProduct.setColor(product.getColor());
        if(product.getBrand() != null) existingProduct.setBrand(product.getBrand());
        if(product.getPrice() != 0) existingProduct.setPrice(product.getPrice());
        if(product.getQuantity() != 0) existingProduct.setQuantity(product.getQuantity());
        if(product.getDiscountedPrice() != 0) existingProduct.setDiscountedPrice(product.getDiscountedPrice());
        if(product.getDiscountedPercent() >= 0) existingProduct.setDiscountedPercent(product.getDiscountedPercent());
        if(product.getSizes() != null) existingProduct.setSizes(product.getSizes());
        if(product.getImageUrl() != null) existingProduct.setImageUrl(product.getImageUrl());

        productRepository.save(existingProduct);
        return existingProduct;
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(() -> new ProductException("Product Not found"));
    }

    @Override
    public List<Product> findProductByCategory(String category) {
        return null;
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);
        if (!colors.isEmpty()) {
            products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor()))).collect(Collectors.toList());
        }

        if (stock != null) {
            if (stock.equals("in_stock")) {
//                TODO: Pending Here
                products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            } else if (stock.equals("out_of_stock")) {
                products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());

            }
        }


        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());
        products = products.subList(startIndex, endIndex);
        Page<Product> filteredProduct = new PageImpl<>(products, pageable, products.size());
        return filteredProduct;
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
