package com.mq.providera.controller;

import com.mq.common.pojo.mail.Mail;
import com.mq.providera.service.MailProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/mail")
@RestController
public class SendMailController {
    @Autowired
    private MailProduceService produceService;


    @GetMapping(value = "/send")
    public String send() {
        Mail mail = new Mail();
        mail.setTo("1134005157@qq.com");
        produceService.send(mail);
        return "SUCCESS";
    }
}
