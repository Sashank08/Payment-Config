package com.sashank.payment.gateway;

import com.sashank.payment.gateway.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.extension.ExtendWith;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

	@InjectMocks
	private JwtService jwtService;

	private String token;

	private static final String SECRET = "mysecretkeymysecretkeymysecretkey12";

	@BeforeEach
	void setUp() {

		Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

		token = Jwts.builder()
				.setSubject("user@gmail.com")
				.claim("role", "USER")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 100000))
				.signWith(key)
				.compact();
	}

	//Test token validation
	@Test
	void testIsTokenValid() {

		boolean valid = jwtService.isTokenValid(token);

		assertTrue(valid);
	}

	//Test email extraction
	@Test
	void testExtractEmail() {

		String email = jwtService.extractEmail(token);

		assertEquals("user@gmail.com", email);
	}

	//Test role extraction
	@Test
	void testExtractRole() {

		String role = jwtService.extractRole(token);

		assertEquals("USER", role);
	}

	//Test invalid token
	@Test
	void testInvalidToken() {

		boolean valid = jwtService.isTokenValid("invalid.token.here");

		assertFalse(valid);
	}
}