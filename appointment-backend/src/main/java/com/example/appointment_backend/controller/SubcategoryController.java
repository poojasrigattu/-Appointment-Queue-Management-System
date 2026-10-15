
package com.example.appointment_backend.controller;

import com.example.appointment_backend.entity.Subcategory;
import com.example.appointment_backend.repository.SubcategoryRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubcategoryController {

    private final SubcategoryRepository subcategoryRepository;

    public SubcategoryController(
            SubcategoryRepository subcategoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
    }

    @GetMapping
    public List<Subcategory> getSubcategories(
            @RequestParam Long categoryId) {
        return subcategoryRepository.findByCategoryId(categoryId);
    }
}
