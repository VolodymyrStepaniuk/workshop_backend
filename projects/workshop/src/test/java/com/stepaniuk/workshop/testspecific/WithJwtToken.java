package com.stepaniuk.workshop.testspecific;


import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@WithSecurityContext(factory = WithJwtTokenSecurityContextFactory.class)
public @interface WithJwtToken {

  long userId();

  String givenName() default "John";

  String familyName() default "Doe";

  String email() default "johndoe@gmail.com";

  String phone() default "1234567890";

  String[] authorities() default {"ROLE_USER"};

  boolean isExpired() default false;
}
