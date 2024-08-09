package org.boot.growup.source.seller.persist.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBrandImage is a Querydsl query type for BrandImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBrandImage extends EntityPathBase<BrandImage> {

    private static final long serialVersionUID = 110977467L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBrandImage brandImage = new QBrandImage("brandImage");

    public final QBrand brand;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath originalImageName = createString("originalImageName");

    public final StringPath path = createString("path");

    public QBrandImage(String variable) {
        this(BrandImage.class, forVariable(variable), INITS);
    }

    public QBrandImage(Path<? extends BrandImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBrandImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBrandImage(PathMetadata metadata, PathInits inits) {
        this(BrandImage.class, metadata, inits);
    }

    public QBrandImage(Class<? extends BrandImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.brand = inits.isInitialized("brand") ? new QBrand(forProperty("brand"), inits.get("brand")) : null;
    }

}

