package com.clinica_medica_Desafio.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clinica_medica_Desafio.model.User;

@Service
public class TokenService {

	private final String secret = "segredinho";
	private final long expirationTimeInMinutes = 120; // 2 horas

	public TokenResponse generateToken(User user) {
		Instant expirationInstant = Instant.now().plus(expirationTimeInMinutes, ChronoUnit.MINUTES);
		Date expirationDate = Date.from(expirationInstant);

		return generateToken(user, expirationDate);
	}

	public TokenResponse generateToken(User user, Date expirationDate) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);

			String token = JWT.create().withIssuer("auth").withSubject(user.getLogin()).withExpiresAt(expirationDate)
					.sign(algorithm);

			Instant expirationInstant = expirationDate.toInstant();
			return new TokenResponse(token, expirationInstant.toString());

		} catch (JWTVerificationException exception) {
			throw new RuntimeException("Erro ao gerar token", exception);
		}
	}

	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
			return decodedJWT.getSubject();
		} catch (JWTVerificationException exception) {
			return null;
		}
	}

	public static class TokenResponse {
		private final String token;
		private final String expiration;

		public TokenResponse(String token, String expiration) {
			this.token = token;
			this.expiration = expiration;
		}

		public String getToken() {
			return token;
		}

		public String getExpiration() {
			return expiration;
		}
	}

}
