package org.boot.growup.common.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.redis.RedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    private final Key key;
    private final RedisDao redisDao;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L; // 1시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7L; // 유효시간 : 일주일
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, RedisDao redisDao){
        this.redisDao = redisDao;
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
    }

    public TokenDto generateToken(String userEmail, Collection<? extends GrantedAuthority> authorities) {
        String authoritiesString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken= Jwts.builder()
                .setSubject(userEmail) // 사용자
                .claim("auth",authoritiesString)
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                .setExpiration(new Date(now.getTime()+ACCESS_TOKEN_EXPIRE_TIME)) // 만료 시간 세팅 (60분)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken=Jwts.builder()
                .setSubject(userEmail) // 사용자
                .setIssuedAt(new Date())
                .setExpiration(expiryDate) // 만료 시간 세팅 (일주일)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        // redis에 저장
        redisDao.setValues(userEmail, refreshToken, REFRESH_TOKEN_EXPIRE_TIME + 5000L);

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
