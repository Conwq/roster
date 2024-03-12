package ru.sergey.patseev.jwtservicespringbootstrarter.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import ru.sergey.patseev.jwtservicespringbootstrarter.constant.JwtClaimName;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

/**
 * Service class for JWT token generation and parsing.
 */
public class JwtService {

	/**
	 * Secret key used for JWT token generation.
	 */
	@Value("${spring.jwt.secret-key}")
	private String secret;

	/**
	 * Expiration time for JWT tokens.
	 */
	@Value("${spring.jwt.expiration}")
	private Long expiration;


	/**
	 * Generates a JWT token with the provided claims and username.
	 *
	 * @param claims   The claims to include in the token.
	 * @param username The username associated with the token.
	 * @return The generated JWT token.
	 */
	public String generateToken(Map<String, Object> claims, String username) {
		return createToken(claims, username);
	}

	/**
	 * Extracts the user ID from the JWT token.
	 *
	 * @param token The JWT token.
	 * @return The user ID extracted from the token.
	 */
	public int extractUserId(String token) {
		return extractClaim(token, claim -> claim.get(JwtClaimName.USER_ID, Integer.class));
	}

	/**
	 * Extracts the user roles from the JWT token.
	 *
	 * @param token The JWT token.
	 * @return The list of user roles extracted from the token.
	 */
	public List<String> extractUserRoles(String token) {
		return extractClaim(token, claims -> claims.get(JwtClaimName.ROLES, List.class));
	}

	/**
	 * Extracts all claims from the JWT token.
	 *
	 * @param token The JWT token.
	 * @return The claims extracted from the token.
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getSignKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	/**
	 * Extracts a specific claim from the JWT token.
	 *
	 * @param token          The JWT token.
	 * @param claimsResolver The function to resolve the desired claim from the claims.
	 * @return The resolved claim.
	 */
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Creates a JWT token with the provided claims and username.
	 *
	 * @param claims   The claims to include in the token.
	 * @param username The username associated with the token.
	 * @return The generated JWT token.
	 */
	private String createToken(Map<String, Object> claims, String username) {
		return Jwts.builder()
				.claims(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignKey(), Jwts.SIG.HS256)
				.compact();
	}

	/**
	 * Retrieves the signing key used for JWT token generation.
	 *
	 * @return The signing key.
	 */
	private SecretKey getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
