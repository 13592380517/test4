package com.yd.zy.emaildemo.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.lang.Nullable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.logging.Logger;

@RestController
public class MailController {

    @Value("${mail.fromMail.sender}")
    private String sender;

    @Value("${mail.fromMail.receiver}")
    private String receiver;

    @Autowired
    private JavaMailSender javaMailSender;

    @RequestMapping("/sendMail")
    public String sendMail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setSubject("xxxx");
        message.setText("aaaaa");
        message.setSentDate(new Date());

        try {
            javaMailSender.send(message);
            System.out.println("2222222222222");
        }catch (Exception e){
            System.out.println("333333333333333"+e);
        }
        return "success";
    }

    @RequestMapping("/sendHtmMail")
    public String testHtmMail(){
        String content="<html>\n"+
                "<body>\n"+
                "<h3>hello world!</h3>\n"+
                "</body>\n"+
                "</html>";
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject("html mail");
            helper.setText(content,true);

            javaMailSender.send(message);
            /*logger.info("html邮件发送成功");*/
        }catch (MessagingException e){
            /*logger.info("发送html邮件时异常");*/
        }
        return "success";
    }

    @RequestMapping("/sendFilesMail")
    public String sendFilesMail() {
        String filePath="/Users/dalaoyang/Downloads/article_tag.sql";
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject("附件邮件");
            helper.setText("这是一封带附件的邮件", true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);

            javaMailSender.send(message);
            /*logger.info("带附件的邮件已经发送。");*/
        } catch (MessagingException e) {
            /*logger.info("发送带附件的邮件时发生异常！");*/
        }
        return "success";
    }

    @RequestMapping("/sendInlineResourceMail")
    public String sendInlineResourceMail() {
        String Id = "dalaoyang12138";
        String content="<html><body>这是有图片的邮件：<img src=\'cid:" + Id + "\' ></body></html>";
        String imgPath = "/Users/dalaoyang/Downloads/dalaoyang.jpeg";
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject("这是有图片的邮件");
            helper.setText(content, true);

            FileSystemResource res = new FileSystemResource(new File(imgPath));
            helper.addInline(Id, res);

            javaMailSender.send(message);
            /*logger.info("嵌入静态资源的邮件已经发送。");*/
        } catch (MessagingException e) {
            /*logger.info("发送嵌入静态资源的邮件时发生异常！");*/
        }
        return "success";
    }

}
