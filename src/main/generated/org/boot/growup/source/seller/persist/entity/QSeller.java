package org.boot.growup.source.seller.persist.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSeller is a Querydsl query type for Seller
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSeller extends EntityPathBase<Seller> {

    private static final long serialVersionUID = 1625166502L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSeller seller = new QSeller("seller");

    public final QBrand brand;

    public final StringPath cpAddress = createString("cpAddress");

    public final StringPath cpCode = createString("cpCode");

    public final StringPath cpEmail = createString("cpEmail");

    public final StringPath cpName = createString("cpName");

    public final StringPath cpPassword = createString("cpPassword");

    public final StringPath epName = createString("epName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> netProceeds = createNumber("netProceeds", Integer.class);

    public final StringPath phoneNumber = createString("phoneNumber");

    public QSeller(String variable) {
        this(Seller.class, forVariable(variable), INITS);
    }

    public QSeller(Path<? extends Seller> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSeller(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSeller(PathMetadata metadata, PathInits inits) {
        this(Seller.class, metadata, inits);
    }

    public QSeller(Class<? extends Seller> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.brand = inits.isInitialized("brand") ? new QBrand(forProperty("brand"), inits.get("brand")) : null;
    }

}

