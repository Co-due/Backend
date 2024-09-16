package soma.edupi.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {


    @Value("${server-url.gateway}")
    private String gatewayUrl;

    private final JavaMailSender emailSender;


    @Async
    public void sendEmail(String toEmail) throws MessagingException {
        MimeMessage emailForm = createEmailForm(toEmail);
        log.warn("MailService.sendEmail exception occur toEmail: {}", toEmail);
        // 이메일 발송
        emailSender.send(emailForm);
    }

    public boolean verifyEmailCode(String email) {
        // TODO: DB의 account 활성화

        return true;
    }

    private MimeMessage createEmailForm(String toEmail) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setSubject("이메일 인증 안내");
        message.setText(setContext(toEmail), "utf-8", "html");

        return message;
    }

    // 이메일 내용 초기화
    private String setContext(String email) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("gatewayUrl", gatewayUrl);

        return templateEngine.process("mail", context);
    }


}
