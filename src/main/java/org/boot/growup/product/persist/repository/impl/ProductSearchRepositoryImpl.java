package org.boot.growup.product.persist.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.boot.growup.product.persist.entity.*;
import org.boot.growup.product.persist.repository.ProductSearchRepository;

import java.util.List;

@RequiredArgsConstructor
public class ProductSearchRepositoryImpl implements ProductSearchRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Product> findProductsByName(String productName) {
        QProduct product = QProduct.product;
        QBrand brand = QBrand.brand;
        QSubCategory subCategory = QSubCategory.subCategory;
        QMainCategory mainCategory = QMainCategory.mainCategory;

        return jpaQueryFactory.selectFrom(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.subCategory, subCategory).fetchJoin()
                .leftJoin(subCategory.mainCategory, mainCategory).fetchJoin()
                .where(
                        product.name.containsIgnoreCase(productName)
                                .or(brand.name.containsIgnoreCase(productName))
                                .or(subCategory.name.containsIgnoreCase(productName))
                                .or(mainCategory.name.containsIgnoreCase(productName))
                )
                .fetch();
    }
}
