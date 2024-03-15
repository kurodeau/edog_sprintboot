package com.token;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.util.JedisUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class TokenRepository {
    private static final int DATABASE_INDEX = 15;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void saveToken(String tokenKey, TokenDTO tokenDTO, Long expiryInSeconds) {
        try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
            jedis.select(DATABASE_INDEX);
            jedis.setex(tokenKey, expiryInSeconds, convertDtoToJson(tokenDTO));
        } catch (JedisException e) {
            e.printStackTrace();
        }
    }


    
    public Optional<TokenDTO> findToken(String tokenKey) {
        try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
            jedis.select(DATABASE_INDEX);
            String tokenStr = jedis.get(tokenKey);
            if(tokenStr!=null) {
                TokenDTO tokenDto = convertJsonToDto(tokenStr);
                return Optional.ofNullable(tokenDto);
            } else {
            	return Optional.empty();
            }
        } catch (JedisException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    

    public void deleteToken(String tokenKey) {
        try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
            jedis.select(DATABASE_INDEX);
            jedis.del(tokenKey);
        } catch (JedisException e) {
            e.printStackTrace();
        }
    }

    public void updateTokenExpiry(String tokenKey, Long expiryInSeconds) {
        try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
            jedis.select(DATABASE_INDEX);
            jedis.expire(tokenKey, expiryInSeconds);
        } catch (JedisException e) {
            e.printStackTrace();
        }
    }

    public boolean isTokenExists(String tokenKey) {
        try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
            jedis.select(DATABASE_INDEX);
            return jedis.exists(tokenKey);
        } catch (JedisException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Set<TokenDTO> getAllTokenKeys() {
        try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
            jedis.select(DATABASE_INDEX);
            
            Set<String> values = jedis.keys("*");
            return  values.stream().map(value -> convertJsonToDto(value)).collect(Collectors.toSet());

        } catch (JedisException e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }

    public Optional<Long> getTokenExpiry(String tokenKey) {
        try (Jedis jedis = JedisUtil.getJedisPool().getResource()) {
            jedis.select(DATABASE_INDEX);
            return Optional.ofNullable(jedis.ttl(tokenKey));
        } catch (JedisException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    public String convertDtoToJson(TokenDTO tokenDto) {

        try {
            return objectMapper.writeValueAsString(tokenDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public TokenDTO convertJsonToDto(String json) {
        try {
            return objectMapper.readValue(json, TokenDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
}
  
