package com.mq.providera.service;

import com.mq.common.pojo.mail.Mail;

public interface MailProduceService {

    boolean send(Mail mail);

}
