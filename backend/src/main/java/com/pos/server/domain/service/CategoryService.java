package com.pos.server.domain.service;

import com.pos.server.domain.Category;
import com.pos.server.domain.Product;
import com.pos.server.domain.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public List<Category> getAll() {return categoryRepository.getAll();}

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Category category,int categoryID ) {
        Optional<Category> optionalCategory = categoryRepository.getCategory(categoryID);

        if (optionalCategory.isPresent()){
            Category existingCategory = optionalCategory.get();
            String categoryImg = category.getImg();
            String fileExtension = "";
            String fileNameWithoutExtension = categoryImg;


            int dotIndex = categoryImg.lastIndexOf(".");

            if (dotIndex > 0) {
                fileNameWithoutExtension = categoryImg.substring(0, dotIndex);
                fileExtension = categoryImg.substring(dotIndex);
            }
            String newFilename = fileNameWithoutExtension + categoryID + fileExtension;

            existingCategory.setImg(newFilename);
            return categoryRepository.save(existingCategory);
        }else {
            throw new RuntimeException("Category not found with id " + categoryID);
        }

    }

}
