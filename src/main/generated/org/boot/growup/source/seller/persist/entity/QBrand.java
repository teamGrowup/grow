package org.boot.growup.source.seller.persist.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBrand is a Querydsl query type for Brand
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBrand extends EntityPathBase<Brand> {

    private static final long serialVersionUID = 1976764288L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBrand brand = new QBrand("brand");

    public final EnumPath<org.boot.growup.source.seller.constant.AuthorityStatus> authorityStatus = createEnum("authorityStatus", org.boot.growup.source.seller.constant.AuthorityStatus.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> likes = createNumber("likes", Integer.class);

    public final StringPath name = createString("name");

    public final QSeller seller;

    public QBrand(String variable) {
        this(Brand.class, forVariable(variable), INITS);
    }

    public QBrand(Path<? extends Brand> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBrand(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBrand(PathMetadata metadata, PathInits inits) {
        this(Brand.class, metadata, inits);
    }

    public QBrand(Class<? extends Brand> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.seller = inits.isInitialized("seller") ? new QSeller(forProperty("seller"), inits.get("seller")) : null;
    }

}

