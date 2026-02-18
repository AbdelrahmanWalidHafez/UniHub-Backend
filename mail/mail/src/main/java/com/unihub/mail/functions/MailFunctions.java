package com.unihub.mail.functions;

import com.unihub.mail.dto.SendAcceptanceMailDto;
import com.unihub.mail.dto.SendVerificationCode;
import com.unihub.mail.service.IMailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class MailFunctions {

    private final IMailService mailService;

    @Bean
    public Consumer<SendAcceptanceMailDto> sendAcceptanceMail () {
        return sendEmail -> {
            try {
                mailService.sendAcceptanceMail(sendEmail.getTo(),sendEmail.getSystemAdminResponse());
            } catch (IOException | MessagingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Bean
    public Consumer<String> sendRejectionMail () {
        return to -> {
            try {
                mailService.sendRejectionMail(to);
            } catch (IOException | MessagingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Bean
    public Consumer<SendVerificationCode> sendVerificationCode(){
        return request -> {
            try {
                mailService.sendVerificationCode(request);
            } catch (IOException | MessagingException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
