package com.stepaniuk.workshop.service.category;

import com.stepaniuk.workshop.testspecific.JpaLevelTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@JpaLevelTest
@Sql(scripts = "classpath:sql/service_categories.sql")
public class ServiceCategoryRepositoryTest {

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    @Test
    void shouldSaveCategory() {

        ServiceCategory serviceCategory = new ServiceCategory(
                null, "New Category", "Description", List.of("url to image"), 100, "new-category", null, null
        );

        ServiceCategory savedCategory = serviceCategoryRepository.save(serviceCategory);

        assertNotNull(savedCategory);
        assertNotNull(savedCategory.getId());

        assertEquals(serviceCategory.getTitle(), savedCategory.getTitle());
        assertEquals(serviceCategory.getDescription(), savedCategory.getDescription());
        assertEquals(serviceCategory.getImageUrls(), savedCategory.getImageUrls());
        assertEquals(serviceCategory.getImageUrls().get(0), savedCategory.getImageUrls().get(0));
        assertEquals(serviceCategory.getPriority(), savedCategory.getPriority());
        assertEquals(serviceCategory.getUrlName(), savedCategory.getUrlName());
    }

    @Test
    void shouldThrowExceptionWhenSavingServiceCategoryWithNullTitle() {
        ServiceCategory serviceCategory = new ServiceCategory(
                null, null, "Description", List.of("url to image"), 100, "new-category", null, null
        );

        assertThrows(DataIntegrityViolationException.class, () -> serviceCategoryRepository.save(serviceCategory));
    }

    @Test
    void shouldFindServiceCategoryById(){
        Optional<ServiceCategory> optionalCategory = serviceCategoryRepository.findById(1L);
        assertTrue(optionalCategory.isPresent());

        ServiceCategory serviceCategory = optionalCategory.get();
        assertNotNull(serviceCategory);

        assertEquals("Title", serviceCategory.getTitle());
        assertEquals("Description", serviceCategory.getDescription());
        assertEquals(List.of("image_url"), serviceCategory.getImageUrls());
        assertEquals("image_url", serviceCategory.getImageUrls().get(0));
        assertEquals(100, serviceCategory.getPriority());
        assertEquals("url_name", serviceCategory.getUrlName());
    }

    @Test
    void shouldUpdateServiceCategory(){
        Optional<ServiceCategory> optionalCategory = serviceCategoryRepository.findById(1L);
        assertTrue(optionalCategory.isPresent());

        ServiceCategory serviceCategory = optionalCategory.get();
        assertNotNull(serviceCategory);
        serviceCategory.setPriority(200);
        ServiceCategory savedCategory = serviceCategoryRepository.save(serviceCategory);
        assertNotNull(savedCategory);
        assertEquals(serviceCategory.getPriority(), savedCategory.getPriority());
        assertEquals(200, savedCategory.getPriority());
    }

    @Test
    void shouldDeleteServiceCategoryByExistingCategory(){
        Optional<ServiceCategory> optionalCategory = serviceCategoryRepository.findById(1L);
        assertTrue(optionalCategory.isPresent());

        ServiceCategory serviceCategory = optionalCategory.get();
        assertNotNull(serviceCategory);
        serviceCategoryRepository.delete(serviceCategory);

        assertFalse(serviceCategoryRepository.existsById(1L));
    }

    @Test
    void shouldDeleteServiceCategoryById(){
        serviceCategoryRepository.deleteById(1L);

        assertFalse(serviceCategoryRepository.existsById(1L));
    }

    @Test
    void shouldReturnListOfServiceCategory(){
        List<ServiceCategory> serviceCategories = serviceCategoryRepository.findAll();

        assertNotNull(serviceCategories);
        assertFalse(serviceCategories.isEmpty());
    }

    @Test
    void shouldReturnPageOfServiceCategory(){
        Page<ServiceCategory> categoryPage = serviceCategoryRepository.findAll(PageRequest.of(0, 2));

        assertNotNull(categoryPage);
        assertFalse(categoryPage.isEmpty());

        List<ServiceCategory> categoryList = categoryPage.getContent();
        assertNotNull(categoryList);
        assertFalse(categoryList.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {150, 200})
    void shouldReturnListOfServiceCategoriesWherePriorityIsUnderOrEqualToParam(int param){
        Specification<ServiceCategory> categorySpecification = Specification.where(
                (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("priority"), param)
        );

        List<ServiceCategory> categoryList = serviceCategoryRepository.findAll(categorySpecification);
        assertNotNull(categoryList);
        assertFalse(categoryList.isEmpty());

        assertAll(
                categoryList.stream().map(
                        category -> () -> assertTrue(category.getPriority() <= param )
                )
        );
    }

}
