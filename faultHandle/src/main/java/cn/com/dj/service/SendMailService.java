package cn.com.dj.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class SendMailService implements InitializingBean {


    private JavaMailSenderImpl mailSender;

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost("pop.mail.163.com");
        mailSender.setUsername("ihndalert@163.com");
        mailSender.setPassword("123456.");
    }

    public void sendMail(String[] receives, String subject, String message) {
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setFrom("ihndalert@163.com");
        simpleMessage.setTo(receives);
        simpleMessage.setSubject(subject);
        simpleMessage.setText(message);
        mailSender.send(simpleMessage);
    }

}
