package com.fct.peluqueria.service.notifications;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotifier {

  private final JavaMailSender mailSender;

  public EmailNotifier(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * metodo que manda un email simple
   * @param to
   * @param subject
   * @param body
   */
  public void sendEmail(String to, String subject, String body) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo(to);
    msg.setSubject(subject);
    msg.setText(body);
    mailSender.send(msg);
  }
}
