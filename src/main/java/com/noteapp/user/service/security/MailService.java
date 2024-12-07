package com.noteapp.user.service.security;

import com.noteapp.user.model.Email;
import java.util.List;

/**
 *
 * @author admin
 */
public interface MailService {
    boolean sendMail(Email toEmail, String subject, String content);
    boolean sendMail(List<Email> toEmails, String subject, String content);
}
