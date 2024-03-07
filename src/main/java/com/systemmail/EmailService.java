//package com.systemmail;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendVerificationEmail(String userEmail, String verificationLink) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(userEmail);
//        message.setSubject("這是您的信箱驗證信件");
//        message.setText("請點這個連結完成您的信箱驗證: " + verificationLink);
//        mailSender.send(message);
//    }
//}
//
