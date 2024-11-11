package com.noteapp.service.security;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author admin
 */
public abstract class VerificationCodeService {
    protected String verificationCode;
    protected String username;
    protected boolean isExpired;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public abstract void generateVerificationCode();

    public boolean verifyCode(String code) {
        return code.equals(verificationCode);
    }
    
    public boolean isExpiredCode() {
        return isExpired;
    }
    
    public void startExpiryCodeTimer() {
        scheduler.schedule(() -> isExpired = true, 3, TimeUnit.MINUTES);
    }
}