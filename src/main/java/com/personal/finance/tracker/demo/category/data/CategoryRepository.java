package com.personal.finance.tracker.demo.category.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personal.finance.tracker.demo.user.data.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    // This interface will automatically provide methods for CRUD operations on Category entities.
    // You can define custom query methods here if needed, such as finding categories by user or name.

    // Example of a custom query method:
    // List<Category> findByUser(AppUser user);

    public Category findByNameAndUserAndDeletableFalse(String name, User appUser);
    public List<Category> findAllByUserAndDeletableFalse(User appUser);
    public Category findByIdAndDeletableFalse(UUID id);

}
