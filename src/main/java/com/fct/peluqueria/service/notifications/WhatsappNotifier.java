package com.fct.peluqueria.service.notifications;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import jakarta.annotation.PostConstruct;

@Component
public class WhatsappNotifier {

  @Value("${twilio.account-sid}")
  private String accountSid;

  @Value("${twilio.auth-token}")
  private String authToken;

  @Value("${twilio.from-whatsapp}")
  private String fromWhatsapp;

  @PostConstruct
  public void init() {
    Twilio.init(accountSid, authToken);
  }
  /**
   * metodo que envia un whatsapp
   * @param toWhatsapp
   * @param body
   */
  public void sendReminder(String toWhatsapp, String body) {
    Message.creator(
        new com.twilio.type.PhoneNumber("whatsapp:" + toWhatsapp),
        new com.twilio.type.PhoneNumber(fromWhatsapp),
        body
    ).create();
  }
}