package com.akamai.socialnetwork.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PostDateValidator.class)
public @interface PostDateConstraint {
    String message() default "Invalid post date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
