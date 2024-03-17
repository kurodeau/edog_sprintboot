package com.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	@Value("${application.security.jwt.secret-key}")
	private String secretKey;
	@Value("${application.security.jwt.expiration}")
	private long jwtExpiration;
	@Value("${application.security.jwt.refresh-token.expiration}")
	private long refreshExpiration;

	private Key getSignInKey() {
		// 將 Base64 編碼的密鑰字串解碼為 byte 陣列
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		// 使用 Keys 工具類將 byte 陣列轉換為 HmacSha 類型的簽名金鑰
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// 這是一個私有方法，用於從 JWT 中提取所有聲明
	private Claims extractAllClaims(String token) {

		// 使用 Jwts 工具類來創建 JWT 解析器建構器
		return Jwts
				// 獲取解析器建構器
				.parserBuilder()
				// 設置解析器的簽名密鑰，用於驗證 JWT 的簽名
				.setSigningKey(getSignInKey())
				// 構建 JWT 解析器
				.build()
				// 解析 JWT 字串，並返回包含所有聲明的 Jws<Claims> 對象
				.parseClaimsJws(token)
				// 從 Jws<Claims> 對象中獲取 JWT 的主體部分，即包含所有聲明的 Claims 對象
				.getBody();
	}

	// 用於從 JWT 中提取特定聲明（claims）的通用方法
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		// 從 JWT 中提取所有聲明
		final Claims claims = extractAllClaims(token);
		// 使用提供的 claimsResolver 函數對 claims 進行處理，並返回處理後的結果
		return claimsResolver.apply(claims);
	}

	// 這個方法用於從 JWT 中提取使用者名稱
	public String extractUsername(String token) {
		// 使用 extractClaim 方法提取 JWT 中的主體聲明（Subject），即使用者名稱
		return extractClaim(token, claims -> claims.getSubject());
		// Claims 代表了 JWT 的聲明部分，而 getSubject 方法用於獲取
		// JWT 的主體聲明（Subject），即表示 JWT 中包含的使用者名稱
	}
	
	
	public List<String> extractRoles(String token) {
	    final Claims claims = extractAllClaims(token);
	    
	    List<Map<String, String>> authorities = (List<Map<String, String>>) claims.get("authorities");
	    
	    List<String> roles = authorities.stream()
	            .map(authority -> authority.get("authority"))
	            .collect(Collectors.toList());
	    
	    return roles;
	}

	// 這是一個私有方法，用於構建 JWT
	private String buildToken(
			// 額外的聲明，可以包含除了預設聲明之外的其他聲明
			Map<String, Object> extraClaims,
			// 使用者詳細資訊，通常用於設置 JWT 的主體（Subject）
			UserDetails userDetails,
			// JWT 的過期時間，以毫秒為單位
			long expiration) {
		// 使用 Jwts 建構 JWT
		 String a =Jwts.builder()
				// 設置 JWT 的聲明，包括額外的聲明、主體、發行時間和到期時間
				// 設置額外的聲明，這些聲明可以包含除了預設聲明之外的其他信息
				.setClaims(extraClaims)
				// 設置 JWT 的主體，通常是使用者名稱
				.setSubject(userDetails.getUsername())
				// 設置 JWT 的發行時間為當前時間
				.setIssuedAt(new Date(System.currentTimeMillis()))
				// 設置 JWT 的到期時間
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				// 使用指定的簽名密鑰進行簽署，使用 HS256 簽名算法
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				// 獲取最終的 JWT 字串表示形式
				.compact();
		 System.out.println(a);
		 
		 return a;
	}

	// 生成帶有額外聲明的 JWT
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		// 調用 buildToken 方法來生成 JWT
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	// 生成 JWT
	public String generateToken(UserDetails userDetails) {
		// 調用 generateToken 方法並傳入空的額外聲明，以及 UserDetails 物件
		return generateToken(new HashMap<>(), userDetails);
	}

	// 生成刷新令牌
	public String generateRefreshToken(UserDetails userDetails) {
		// 調用 buildToken 方法來生成刷新令牌，不含任何額外聲明
		return buildToken(new HashMap<>(), userDetails, refreshExpiration);
	}

	// 這個方法用於驗證 JWT 是否有效
	public boolean isTokenValid(String token, UserDetails userDetails) {
		// 提取 JWT 中的使用者名稱
		final String username = extractUsername(token);
		// 檢查使用者名稱是否與給定的 UserDetails 物件中的使用者名稱相符，並檢查令牌是否過期
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	// 這是一個私有方法，用於從 JWT 中提取過期時間
	private Date extractExpiration(String token) {
		// 使用 extractClaim 方法提取 JWT 中的過期時間聲明
		return extractClaim(token, Claims::getExpiration);
	}

	// 這是一個私有方法，用於檢查 JWT 是否已過期
	private boolean isTokenExpired(String token) {
		// 提取 JWT 中的過期時間，並與當前時間進行比較
		return extractExpiration(token).before(new Date());
	}

}
