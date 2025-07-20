package com.pos.server.web.controller;
import com.pos.server.domain.model.Category;
import com.pos.server.domain.model.Product;
import com.pos.server.domain.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    @ApiOperation("Get all categories")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<Category>> getAll (){
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<Category> update (@RequestBody Category category, @PathVariable("id") int categoryId){

        return new ResponseEntity<>(categoryService.update(category, categoryId), HttpStatus.OK);
    }
}
