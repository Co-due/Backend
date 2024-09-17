package soma.edupi.user.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import soma.edupi.user.domain.Account;
import soma.edupi.user.domain.Role;
import soma.edupi.user.dto.response.TokenInfo;

@Component
public class TokenProvider {

    private final SecretKey key;
    private final long expiration;

    public TokenProvider(@Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration}") long expiration) {
        this.key = generateKey(secret);
        this.expiration = expiration;
    }

    public String generateToken(Account member) {
        long now = (new Date()).getTime();
        Date tokenExpiration = new Date(now + expiration);

        return Jwts.builder()
            .claim("accountId", member.getId())
            .claim("email", member.getEmail())
            .claim("name", member.getName())
            .claim("role", member.getRole())
            .signWith(key, SIG.HS512)
            .expiration(tokenExpiration)
            .compact();
    }

    public TokenInfo findAccountInfoBy(String token) {
        Claims claims = getClaims(token);

        isTokenExpired(claims); // 토큰이 유효한지 검사

        return TokenInfo.builder()
            .email(claims.get("email", String.class))
            .name(claims.get("name", String.class))
            .role(Role.valueOf(claims.get("role", String.class)))
            .build();
    }

    private SecretKey generateKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getClaims(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("토큰이 없습니다.");
        }

        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private void isTokenExpired(Claims claims) {
        if (claims.getExpiration().before(new Date())) {
            throw new JwtException("토큰이 만료되었습니다.");
        }
    }

}
