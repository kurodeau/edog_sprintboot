//package com.util;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//
//public class TestLocalDateTime {
//    public static void main(String[] args) throws Exception {
//    	LocalDateTime dateTime = LocalDateTime.now(); // 假設這是你的 LocalDateTime 物件
//
//        ObjectMapper mapper = new ObjectMapper();
//        SimpleModule module = new SimpleModule();
//        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
//            @Override
//            public void serialize(LocalDateTime dateTime, JsonGenerator jsonGenerator, com.fasterxml.jackson.databind.SerializerProvider serializerProvider) throws IOException {
//                jsonGenerator.writeString(dateTime.format(DateTimeFormatter.ISO_DATE_TIME));
//            }
//        });
//        mapper.registerModule(module);
//
//        String json = mapper.writeValueAsString(dateTime);
//
//        System.out.println(json);
//    }
//}
//
