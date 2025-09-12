package com.eplugger.config.security;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import com.eplugger.business.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret:defaultSecretKeyWhichShouldBeAtLeast32CharactersLong}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    @Value("${jwt.clock-skew:300}")
    private Long clockSkewSeconds;

    private static final TimedCache<String, User> TOKEN_USER_CACHE = CacheUtil.newTimedCache(DateUnit.MINUTE.getMillis() * 120);

    /**
     * 普通缓存放入并设置时间
     *
     * @param token    用户认证键
     * @param userInfo 用户对象
     * @param time     时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */
    public void set(String token, User userInfo, long time) {
        TOKEN_USER_CACHE.put(token, userInfo, DateUnit.SECOND.getMillis() * time);
    }

    /**
     * 通过token获取用户信息
     * <p>获取指定token后会自动续期</p>
     *
     * @param token 用户认证键
     * @return 用户对象
     */
    public User get(String token) {
        return StringUtils.isBlank(token) ? null : TOKEN_USER_CACHE.get(token);
    }

    public String generateToken(Authentication authentication) {
        String token = createToken(authentication);
        TOKEN_USER_CACHE.put(token, (User) authentication.getPrincipal());
        return token;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(clockSkewSeconds)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        Date now = new Date();
        boolean isExpired = expiration.before(new Date(now.getTime() - (clockSkewSeconds * 1000)));

        log.debug("Token expiration check - Expiration: {}, Current time: {}, Clock skew: {}s, Is expired: {}",
                expiration, now, clockSkewSeconds, isExpired);

        return isExpired;
    }

    private String createToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
} 