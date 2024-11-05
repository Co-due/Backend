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
import soma.edupiuser.account.models.LogoutResponse;
import soma.edupiuser.account.models.SignupRequest;
import soma.edupiuser.account.models.SignupResponse;
import soma.edupiuser.account.models.TokenInfo;
import soma.edupiuser.account.service.AccountService;
import soma.edupiuser.account.service.EmailService;
import soma.edupiuser.oauth.models.OAuth2Provider;
import soma.edupiuser.web.utils.CookieUtils;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/account")
public class AccountController implements AccountOpenApi {

    private final EmailService emailService;
    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody AccountLoginRequest accountLoginRequest,
        HttpServletResponse response) {
        String token = accountService.login(accountLoginRequest);
        CookieUtils.addCookie(response, "token", token, 60 * 60);

        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    @GetMapping("/login/info")
    public ResponseEntity<TokenInfo> loginInfo(@CookieValue("token") String token) {
        TokenInfo tokenInfo = accountService.findAccountInfo(token);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(tokenInfo);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> createAccount(@Valid @RequestBody SignupRequest signupRequest)
        throws MessagingException {
        accountService.signup(signupRequest);
        emailService.sendEmail(signupRequest.getEmail());

        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }


    // 이메일 인증
    @PostMapping("/activate")
    public ResponseEntity<Void> activateAccount(@RequestBody EmailRequest emailRequest) {
        emailService.activateAccount(emailRequest);
        log.info("Account: 이메일 인증 완료 {}", emailRequest.getEmail());

        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }

    @GetMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request, HttpServletResponse response,
        @CookieValue("token") String token) {
        TokenInfo tokenInfo = accountService.findAccountInfo(token);
        String provider = tokenInfo.getProvider();

        CookieUtils.deleteCookie(request, response, "token");

        return ResponseEntity
            .status(HttpStatus.OK)
            .body((new LogoutResponse(OAuth2Provider.isOauth(provider), provider)));
    }
}
