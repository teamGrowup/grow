package org.boot.growup.product.persist.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.boot.growup.product.persist.entity.Product;
import org.boot.growup.product.persist.entity.QProduct;
import org.boot.growup.product.persist.repository.ProductSearchRepository;

import java.util.List;
@RequiredArgsConstructor
public class ProductSearchRepositoryImpl implements ProductSearchRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Product> findProductsByName(String productName) {
        QProduct product = QProduct.product;

        return jpaQueryFactory.selectFrom(product)
                .where(product.name.containsIgnoreCase(productName))
                .fetch();
    }
}
