package com.finebk.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("4BSQf1iKqOePYL7Q7gZK5s9V8fXa3h2m+1j4KLm8QnI=")
	private String jwtSecret;

	@Value("3600000")
	private int jwtExpirationInMs;

	private SecretKey signingKey;

	@PostConstruct
	private void init() {
		byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
		this.signingKey = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(Authentication authentication) {
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		return Jwts.builder()
				.setSubject(Long.toString(principal.getId()))
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(signingKey)
				.compact();
	}

	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		return Long.valueOf(claims.getSubject());
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser()
					.setSigningKey(signingKey)
					.build()
					.parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			LOGGER.error("Invalid JWT signature", ex);
		} catch (MalformedJwtException ex) {
			LOGGER.error("Invalid JWT token", ex);
		} catch (ExpiredJwtException ex) {
			LOGGER.error("Expired JWT token", ex);
		} catch (UnsupportedJwtException ex) {
			LOGGER.error("Unsupported JWT token", ex);
		} catch (IllegalArgumentException ex) {
			LOGGER.error("JWT claims string is empty", ex);
		}
		return false;
	}
}
