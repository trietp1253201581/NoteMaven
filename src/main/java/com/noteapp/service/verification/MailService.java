package com.noteapp.service.verification;

import com.noteapp.model.datatransfer.Email;
import java.util.List;

/**
 *
 * @author admin
 */
public interface MailService {
    boolean sendMail(Email toEmail, String subject, String content);
    boolean sendMail(List<Email> toEmails, String subject, String content);
}
