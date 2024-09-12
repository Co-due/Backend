package soma.edupi.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soma.edupi.user.dto.request.EmailRequest;
import soma.edupi.user.service.EmailService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/email")
public class EmailController {

    private final EmailService emailService;

    // 인증코드 인증
    @PostMapping("/verify")
    public String verify(EmailRequest emailRequest) {

        boolean isVerify = emailService.verifyEmailCode(emailRequest.getEmail());
        log.info("EmailController: 이메일 인증 완료 {}", emailRequest.getEmail());
        return isVerify ? "인증이 완료되었습니다." : "인증 실패하셨습니다.";
    }
}
