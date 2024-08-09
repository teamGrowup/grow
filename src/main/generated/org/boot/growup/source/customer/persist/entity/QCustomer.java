package org.boot.growup.source.customer.persist.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCustomer is a Querydsl query type for Customer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomer extends EntityPathBase<Customer> {

    private static final long serialVersionUID = -1925569692L;

    public static final QCustomer customer = new QCustomer("customer");

    public final StringPath address = createString("address");

    public final StringPath birthday = createString("birthday");

    public final StringPath email = createString("email");

    public final EnumPath<org.boot.growup.common.enumerate.Gender> gender = createEnum("gender", org.boot.growup.common.enumerate.Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final EnumPath<org.boot.growup.common.oauth2.Provider> provider = createEnum("provider", org.boot.growup.common.oauth2.Provider.class);

    public final EnumPath<org.boot.growup.common.enumerate.Role> role = createEnum("role", org.boot.growup.common.enumerate.Role.class);

    public QCustomer(String variable) {
        super(Customer.class, forVariable(variable));
    }

    public QCustomer(Path<? extends Customer> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCustomer(PathMetadata metadata) {
        super(Customer.class, metadata);
    }

}

