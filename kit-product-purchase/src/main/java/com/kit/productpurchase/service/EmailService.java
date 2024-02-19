package com.kit.productpurchase.service;

import com.kit.productpurchase.constant.AppConstant;
import com.kit.productpurchase.model.request.EmailRequest;
import com.kit.productpurchase.model.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    public CommonResponse sendEmail(EmailRequest request, String uniqueInteractionId) {
        try {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("smtp.gmail.com");
            mailSender.setPort(587);
            mailSender.setUsername(AppConstant.MAIL_URL);
            mailSender.setPassword(AppConstant.MAIL_PASSWORD);

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.starttls.enable", "true");

            mailSender.setJavaMailProperties(properties);


            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(AppConstant.MAIL_URL);
            helper.setTo(request.getRecipient());
            helper.setSubject(request.getSubject());


            boolean html = true;
            helper.setText(request.getBody(), html);

            mailSender.send(message);

            return CommonResponse.builder()
                    .response(AppConstant.SUCCESS)
                    .build();
        } catch (Exception ex) {
            log.info("interactionId : {} , errorMsg : {} ", uniqueInteractionId, ex.getMessage());
            return CommonResponse.builder()
                    .response(AppConstant.FAILED + " error : " + ex.getMessage())
                    .build();
        }
    }
}
