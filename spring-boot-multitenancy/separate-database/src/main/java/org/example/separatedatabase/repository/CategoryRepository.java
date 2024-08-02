package org.example.separatedatabase.repository;

import org.example.separatedatabase.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
