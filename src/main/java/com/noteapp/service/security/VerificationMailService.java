package com.noteapp.service.security;

import com.noteapp.model.Email;

/**
 *
 * @author admin
 */
public class VerificationMailService {
    protected MailService mailService;
    protected VerificationCodeService verificationCodeService;
    protected CodeStatus codeStatus;
    
    public static enum CodeStatus {
        EXPIRED, TRUE, FALSE
    }

    public VerificationMailService(MailService mailService, VerificationCodeService verificationCodeService) {
        this.mailService = mailService;
        this.verificationCodeService = verificationCodeService;
    }
    
    public void sendCode(Email toEmail) {
        String subject = "Verification Code for Note App";
        verificationCodeService.generateVerificationCode();
        String verificationCode = verificationCodeService.getVerificationCode();
        String content = "Your verification code is " + verificationCode;
        mailService.sendMail(toEmail, subject, content);
    }
    
    public void checkCode(String inputCode) {
        if(verificationCodeService.isExpiredCode()) {
            codeStatus = CodeStatus.EXPIRED;
        } else if (!verificationCodeService.verifyCode(inputCode)) {
            codeStatus = CodeStatus.FALSE;
        } else {
            codeStatus = CodeStatus.TRUE;
        }
    }

    public CodeStatus getCodeStatus() {
        return codeStatus;
    }
}