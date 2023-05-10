package com.project.raif.services;

import com.project.raif.email.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details, boolean b);
    String sendMailWithAttachment(EmailDetails details);
}
