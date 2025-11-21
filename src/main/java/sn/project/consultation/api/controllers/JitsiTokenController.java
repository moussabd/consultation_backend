package sn.project.consultation.api.controllers;



import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class JitsiTokenController {

    @Value("${jaas.api.key}")
    private String apiKey;

    @Value("${jaas.api.secret}")
    private String apiSecret;

    @Value("${jaas.api.id}")
    private String appId;

    @GetMapping("/jitsi-token")
    public ResponseEntity<Map<String,String>> getJitsiToken(
            @RequestParam String roomName,
            @RequestParam String userName,
            @RequestParam(required = false) String userEmail
    ){
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiration = Date.from(now.plusSeconds(3600));

        Map<String,Object> contextUser = new HashMap<>();
        contextUser.put("name", userName);
        if (userEmail != null) {
            contextUser.put("email", userEmail);
        }

        Map<String,Object> context = new HashMap<>();
        context.put("user", contextUser);

        Map<String,Object> claims = new HashMap<>();
        claims.put("aud", "jitsi");
        claims.put("iss", "chat");
        claims.put("sub", appId);
        claims.put("room", roomName);
        claims.put("context", context);

        SecretKey key = Keys.hmacShaKeyFor(apiSecret.getBytes());
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Map<String,String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
