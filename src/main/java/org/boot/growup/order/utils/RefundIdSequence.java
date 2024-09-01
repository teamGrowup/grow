package org.boot.growup.order.utils;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * RefundID를 생성함.
 */
@IdGeneratorType(RefundIdGenerator.class)
@Retention(RUNTIME) @Target({METHOD,FIELD})
public @interface RefundIdSequence {
}
