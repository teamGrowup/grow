package org.boot.growup.common.jwt;

import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.redis.RedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

@Component
@Slf4j
public class JwtTokenProvider {
    private final Key key;
    private final RedisDao redisDao;
    private static final int JWT_EXPIRATION_MS = 604800000; // 유효시간 : 일주일
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, RedisDao redisDao){
        this.redisDao = redisDao;
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
    }
}
