package fotcamp.finhub.common.utils;


import fotcamp.finhub.common.security.TokenDto;
import fotcamp.finhub.common.exception.ErrorMessage;
import fotcamp.finhub.main.domain.RoleType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

/** JWT 생성, 유효성 검증, 클레임 추출 메소드 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;

    @Autowired
    public JwtUtil(
            @Value("${jwt.key}") String secretKey, @Value("${jwt.accessTokenExpirationTime}") long accessTokenExpTime, @Value("${jwt.refreshTokenExpirationTime}") long refreshTokenExpTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    /**
     * Access Token 생성
     * @param memberId
     * @return TokenDto
     */
    public TokenDto createAllTokens(Long memberId, String roleType){
        return new TokenDto(createToken(memberId, roleType, "Access"), createToken(memberId,roleType, "Refresh"));
    }

    /**
     * JWT 생성
     * @param memberId, type
     * @return JWT String
     */
    public String createToken(Long memberId, String roleType, String type){
        long expireTime = type.equals("Access") ? accessTokenExpTime : refreshTokenExpTime;

        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);
        claims.put("role", roleType);
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
     * 토큰에서 RoleType 추출
     * @param token
     * @return RoleType
     * */
    public String getRoleType(String token){
        return parseClaims(token).get("role", String.class);
    }

    /**
     * JWT Claims 추출
     * @param token
     * @return JWT Claims
     * */
    public Claims parseClaims(String token){
        return  Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
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
            throw new JwtException(ErrorMessage.WRONG_TYPE_TOKEN.getMsg()); //잘못된 토큰
        } catch (ExpiredJwtException e){
            log.info("Expired JWT Token", e);
            throw new JwtException(ErrorMessage.EXPIRED_TOKEN.getMsg()); // 만료된 토큰
        } catch (UnsupportedJwtException e){
            log.info("Unsupported JWT Token", e);
            throw new JwtException(ErrorMessage.UNSUPPORTED_TOKEN.getMsg()); //지원되지 않는 토큰
        } catch (IllegalStateException e){
            log.info("JWT claims string is empty", e);
            throw new JwtException(ErrorMessage.UNKNOWN_ERROR.getMsg()); // 빈 토큰
        }
    }

    // 컨트롤러 계층에서 활용하는 토큰 검증 메소드
    public boolean validateTokenServiceLayer(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
//            log.info("Token validation error", e);
            return false;
        }
    }

    /**
     * 헤더 정보 추출
     * @param request
     * @return token
     * */
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        else {
            return null;
        }
    }
}
