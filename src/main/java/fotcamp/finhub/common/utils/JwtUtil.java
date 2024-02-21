package fotcamp.finhub.common.utils;


import fotcamp.finhub.common.dto.CustomUserInfoDto;
import fotcamp.finhub.common.exception.TokenNotValidateException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.ZonedDateTime;

/** JWT 생성, 유효성 검증, 클레임 추출 메소드 */
@Component
@Slf4j
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpTime;

    public JwtUtil(
            @Value("${jwt.key}") String secretKey, @Value("${jwt.tokenExpirationTime}") long accessTokenExpTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    /**
     * Access Token 생성
     * @param member
     * @return Access Token String
     */
    public String createAccessToken(CustomUserInfoDto member){
        return createToken(member,accessTokenExpTime);
    }

    /**
     * JWT 생성
     * @param member
     * @return JWT String
     */
    public String createToken(CustomUserInfoDto member, long expireTime){
        Claims claims = Jwts.claims();
        System.out.println("custom member id"+ member.getMemberId());
        System.out.println(member.getRole());
        claims.put("memberId", member.getMemberId());
        claims.put("role", member.getRole());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰에서 User ID 추출
     * @param token
     * @return User ID
     * */
    public Long getUserId(String token){
        return parseClaims(token).get("memberId", Long.class);
    }

    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     * */
    public Claims parseClaims(String accessToken){
        try{
            return  Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    /**
     * JWT 검증
     * @param token
     * @return IsValidate
     * */
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e){
            log.info("Invalid JWT Token", e);
            throw new TokenNotValidateException("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e){
            log.info("Expired JWT Token", e);
            throw new TokenNotValidateException("만료된 토큰입니다.", e);
        } catch (UnsupportedJwtException e){
            log.info("Unsupported JWT Token", e);
            throw new TokenNotValidateException("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalStateException e){
            log.info("JWT claims string is empty", e);
            throw new TokenNotValidateException("잘못된 JWT 토큰입니다.", e);
        }
    }
}
