package com.unihub.mail.service.impl;

import com.unihub.mail.dto.SystemAdminResponse;
import com.unihub.mail.service.IMailService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements IMailService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void sendAcceptanceMail(String to, SystemAdminResponse systemAdminResponse) throws IOException, MessagingException {
        MimeMessage message = createMimeMessage(to," your request is accepted");
        message.setContent(populateTemplate(systemAdminResponse),"text/html");
        mailSender.send(message);
    }

    @Async
    @Override
    public void sendRejectionMail(String to) throws IOException, MessagingException {
        MimeMessage message = createMimeMessage(to," your request is accepted");
        message.setContent(fetchTemplate("rejection.html"),"text/html");
        mailSender.send(message);
    }

    private MimeMessage createMimeMessage(String to,String subject) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        return message;
    }

    private String populateTemplate(SystemAdminResponse adminResponse) throws IOException {
        String htmlTemplate = fetchTemplate("acceptance.html");
        return htmlTemplate
                .replace("{{Email}}",adminResponse.getEmail())
                .replace("{{Password}}",adminResponse.getPassword())
                .replace("{{Gender}}",adminResponse.getGender().toString())
                .replace("{{UniversityID}}",adminResponse.getUniversityMetadata().getTid().toString())
                .replace("{{Role}}",adminResponse.getRole().getName());
    }

    private String fetchTemplate(String fileName) throws IOException {
        return  readFile(fileName);
    }

    private String readFile(String fileName) throws IOException {
        var template=getClass().getClassLoader().getResourceAsStream("templates/"+fileName);
        assert template != null;
        return new String(template.readAllBytes(), StandardCharsets.UTF_8);
    }
}
