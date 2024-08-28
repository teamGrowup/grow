package org.boot.growup.product.persist.repository;

import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.product.persist.entity.Product;
import org.boot.growup.product.persist.entity.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    Optional<ProductLike> findByCustomerAndProduct(Customer customer, Product product);
}
