package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.source.customer.persist.entity.Customer;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {
    Optional<ProductLike> findByCustomerAndProduct(Customer customer, Product product);
}
