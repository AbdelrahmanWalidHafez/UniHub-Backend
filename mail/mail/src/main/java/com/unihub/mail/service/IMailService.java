package com.unihub.mail.service;

import com.unihub.mail.dto.SystemAdminResponse;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface IMailService {

    void sendAcceptanceMail(String to, SystemAdminResponse systemAdminResponse) throws IOException, MessagingException;

    void sendRejectionMail(String to) throws IOException, MessagingException;
}
