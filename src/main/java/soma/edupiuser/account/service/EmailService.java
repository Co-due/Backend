package soma.edupiuser.account.service;

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
import soma.edupiuser.account.models.EmailRequest;
import soma.edupiuser.web.client.MetaServerApiClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {


    @Value("${site-url.base}")
    private String baseUrl;

    private final JavaMailSender emailSender;
    private final MetaServerApiClient metaServerApiClient;


    @Async
    public void sendEmail(String toEmail) throws MessagingException {
        MimeMessage emailForm = createEmailForm(toEmail);
        emailSender.send(emailForm);
        log.info("MailService.sendEmail: toEmail: {}", toEmail);

    }

    public void activateAccount(EmailRequest emailRequest) {
        metaServerApiClient.activate(emailRequest);
    }

    private MimeMessage createEmailForm(String toEmail) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
        message.setSubject("이메일 인증 안내");
        message.setText(setContext(toEmail), "utf-8", "html");

        return message;
    }

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
        context.setVariable("baseUrl", baseUrl);
        context.setVariable("loginUrl", baseUrl + "/login");

        return templateEngine.process("mail", context);
    }


}
