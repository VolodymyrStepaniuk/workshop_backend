package com.stepaniuk.workshop.testspecific;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class WithJwtTokenSecurityContextFactory implements
    WithSecurityContextFactory<WithJwtToken> {

  private final JwtEncoder jwtEncoder;

  private final Converter<Jwt, AbstractAuthenticationToken> converter;

  public WithJwtTokenSecurityContextFactory(JwtEncoder jwtEncoder,
      Converter<Jwt, AbstractAuthenticationToken> converter) {
    this.jwtEncoder = jwtEncoder;
    this.converter = converter;
  }

  @Override
  public SecurityContext createSecurityContext(WithJwtToken annotation) {

    Instant now = Instant.now();

    Instant issuedAt = annotation.isExpired() ? now.minus(2, ChronoUnit.MINUTES) : now;
    Instant expiresAt =
        annotation.isExpired() ? now.minus(1, ChronoUnit.MINUTES) : now.plus(2, ChronoUnit.MINUTES);

    // @formatter:off
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuedAt(issuedAt)
        .expiresAt(expiresAt)
        .subject(String.valueOf(annotation.userId()))
        .claim("given_name", annotation.givenName())
        .claim("family_name", annotation.familyName())
        .claim("email", annotation.email())
        .claim("phone", annotation.phone())
        .claim("authorities", List.of(annotation.authorities()))
        .build();
    // @formatter:on

    Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));

    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

    Authentication authentication = converter.convert(jwt);

    securityContext.setAuthentication(authentication);

    return securityContext;
  }


}
