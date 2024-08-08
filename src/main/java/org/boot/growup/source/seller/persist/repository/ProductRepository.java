package org.boot.growup.source.seller.persist.repository;


import org.boot.growup.source.seller.persist.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}