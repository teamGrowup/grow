package org.boot.growup.product.persist.repository.impl;

import com.querydsl.core.BooleanBuilder;
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

        String[] keywords = productName.split("\\s+");

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String keyword : keywords) {
            booleanBuilder.or(product.name.containsIgnoreCase(keyword))
                    .or(brand.name.containsIgnoreCase(keyword))
                    .or(subCategory.name.containsIgnoreCase(keyword))
                    .or(mainCategory.name.containsIgnoreCase(keyword));
        }

        return jpaQueryFactory.selectFrom(product)
                .leftJoin(product.brand, brand).fetchJoin()
                .leftJoin(product.subCategory, subCategory).fetchJoin()
                .leftJoin(subCategory.mainCategory, mainCategory).fetchJoin()
                .where(booleanBuilder)
                .fetch();
    }
}
