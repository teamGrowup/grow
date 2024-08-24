package org.boot.growup.auth.utils;

import io.jsonwebtoken.*;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.Provider;
import org.boot.growup.common.model.TokenDTO;
import org.boot.growup.common.model.RedisDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private final Key key;
    private final RedisDAO redisDao;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L; // 1시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7L; // 유효시간 : 일주일

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, RedisDAO redisDao){
        this.redisDao = redisDao;
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
    }

    public TokenDTO generateToken(String userEmail, Collection<? extends GrantedAuthority> authorities) {
        String authoritiesString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken= Jwts.builder()
                .setSubject(userEmail)
                .claim(AUTHORITIES_KEY,authoritiesString)
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime()+ACCESS_TOKEN_EXPIRE_TIME)) // 만료 시간 : 60분
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken=Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate) // 만료 시간 세팅 : 일주일
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        // redis에 저장
        redisDao.setValues(userEmail, refreshToken, REFRESH_TOKEN_EXPIRE_TIME + 5000L);

        return TokenDTO.of(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw e;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw e;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw e;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
