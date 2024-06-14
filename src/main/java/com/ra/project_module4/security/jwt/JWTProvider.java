package com.ra.project_module4.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JWTProvider {

    @Value("${jwt_expiration}")
    private int jwt_expiration;
    @Value("${jwt_secret}")
    private String jwt_secret;

    //Hàm để tạo chuỗi token từ thông tin tài khoản
    public String generateToken(UserDetails userDetails){
        Date today = new Date();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())  //Lấy username để tạo chuỗi token
                .setIssuedAt(today) //Thời gian bắt đầu có hiệu lưực
                .setExpiration(new Date(today.getTime()+jwt_expiration)) //Thời gian hết hiêu lực
                .signWith(SignatureAlgorithm.HS384,jwt_secret) //Mã hóa chữ ký với chuỗi token
                .compact();
    }

    //hàm phân tích chuỗi token truyền đến
    public boolean validateToken(String token){
        try {
            //Phân tích chuỗi token
            Jwts.parser().setSigningKey(jwt_secret).parseClaimsJws(token);
            //Không có lỗi thì trả về true
            return true;
        }catch (ExpiredJwtException e){
            log.error("JWT da het han");
        }catch (UnsupportedJwtException e){
            log.error("Server khong ho tro xu ly jwt");
        }catch (MalformedJwtException e){
            log.error("JWT khong dung dinh dang");
        }catch (SignatureException e){
            log.error("Khong dung chu ky");
        }catch (IllegalArgumentException e){
            log.error("Tham so truyen den khong hop le");
        }
        //Có lỗi trả về false
        return false;
    }

    //Hàm lấy username  từ chuỗi token
    public String getUsernameFromToken(String token){
        return Jwts.parser().setSigningKey(jwt_secret).parseClaimsJws(token).getBody().getSubject();
    }
}
