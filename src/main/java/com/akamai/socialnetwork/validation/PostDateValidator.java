package com.akamai.socialnetwork.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Date;

public class PostDateValidator implements ConstraintValidator<PostDateConstraint, Date> {

    @Override
    public void initialize(PostDateConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Date postDate, ConstraintValidatorContext context) {
        if (postDate == null) {
            return false;
        }
        Date currentDate = new Date();

        // comparing 2 dates (postDate and currentDate) with the precision of seconds
        // while taking network latency into account
        return postDate.getTime() / 1000 == currentDate.getTime() / 1000;
    }
}
