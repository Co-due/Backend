package soma.edupiuser.account;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soma.edupiuser.account.models.AccountLoginRequest;
import soma.edupiuser.account.models.EmailRequest;
import soma.edupiuser.account.models.SignupRequest;
import soma.edupiuser.account.models.TokenInfo;
import soma.edupiuser.account.service.AccountService;
import soma.edupiuser.account.service.EmailService;
import soma.edupiuser.web.models.SuccessResponse;
import soma.edupiuser.web.utils.CookieUtils;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/account")
public class AccountController implements AccountOpenApi {

    private final EmailService emailService;
    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse> login(@Valid @RequestBody AccountLoginRequest accountLoginRequest,
        HttpServletResponse response) {
        String token = accountService.login(accountLoginRequest);
        CookieUtils.addCookie(response, "token", token, 60 * 60);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SuccessResponse.withDetail("Success login"));
    }

    @GetMapping("/login/info")
    public ResponseEntity<SuccessResponse> loginInfo(@CookieValue("token") String token) {
        TokenInfo tokenInfo = accountService.findAccountInfo(token);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SuccessResponse.withDetailAndResult("Success login info", tokenInfo));
    }

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> createAccount(@Valid @RequestBody SignupRequest signupRequest)
        throws MessagingException {
        accountService.signup(signupRequest);
        emailService.sendEmail(signupRequest.getEmail());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SuccessResponse.withDetail("Success logout"));
    }


    // 이메일 인증
    @PostMapping("/activate")
    public ResponseEntity<SuccessResponse> activateAccount(@RequestBody EmailRequest emailRequest) {
        emailService.activateAccount(emailRequest);
        log.info("success email activate : email={}", emailRequest.getEmail());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SuccessResponse.withDetail("Success activate email"));
    }

    @GetMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, "token");

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(SuccessResponse.withDetail("Success logout"));
    }
}
