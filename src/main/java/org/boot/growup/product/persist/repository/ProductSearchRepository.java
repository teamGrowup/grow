package org.boot.growup.product.persist.repository;

import org.boot.growup.product.persist.entity.Product;

import java.util.List;

public interface ProductSearchRepository {
    List<Product> findProductsByName(String productName);
}
