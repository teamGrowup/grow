package org.boot.growup.product.persist.repository;

import org.boot.growup.product.persist.entity.Product;
import org.boot.growup.product.persist.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSearchRepository {
    List<Product> findProductsByName(String productName);
}
