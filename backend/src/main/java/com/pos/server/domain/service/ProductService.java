package com.pos.server.domain.service;

import com.pos.server.domain.model.Product;
import com.pos.server.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Cacheable(value = "products", key = "'all'")
    public List<Product> getAll() {
        return productRepository.getAll();
    }

    @Cacheable(value = "products", key = "#productId")
    public Optional<Product> getProduct(int productId) {
        return productRepository.getProduct(productId);
    }

    @Cacheable(value = "productsByCategory", key = "#categoryId")
    public Optional<List<Product>> getByCategory(int categoryId) {
        return productRepository.getByCategory(categoryId);
    }

    @CacheEvict(value = {"products", "productsByCategory"}, allEntries = true)
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @CacheEvict(value = {"products", "productsByCategory"}, allEntries = true)
    public Product update (Product product, int productID) {
            Optional<Product> optionalProduct = productRepository.getProduct(productID);

            if (optionalProduct.isPresent()) {
                Product existingProduct = optionalProduct.get();

                String originalFilename = product.getImg_url();
                String fileExtension = "";
                String fileNameWithoutExtension = originalFilename;


                int dotIndex = originalFilename.lastIndexOf(".");

                if (dotIndex > 0) {
                    fileNameWithoutExtension = originalFilename.substring(0, dotIndex);
                    fileExtension = originalFilename.substring(dotIndex);
                }
                String newFilename = fileNameWithoutExtension + productID + fileExtension;
                existingProduct.setImg_url(newFilename);


                return productRepository.save(existingProduct);
            } else {
                throw new RuntimeException("Product not found with id " + productID);
            }
    }

    @CacheEvict(value = {"products", "productsByCategory"}, allEntries = true)
    public boolean delete(int productId) {
        return getProduct(productId).map(product -> {
            productRepository.delete(productId);
            return true;
        }).orElse(false);
    }
}
