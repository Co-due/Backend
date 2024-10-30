package soma.edupiuser.web.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import soma.edupiuser.account.models.AccountLoginRequest;
import soma.edupiuser.account.models.EmailRequest;
import soma.edupiuser.account.models.SignupRequest;
import soma.edupiuser.account.models.SignupResponse;
import soma.edupiuser.account.service.domain.Account;
import soma.edupiuser.oauth.models.SignupOauthRequest;

@Component
@HttpExchange("/v1/account")
public interface MetaServerApiClient {

    @PostExchange("/login")
    Account login(@RequestBody AccountLoginRequest accountLoginRequest);

    @PostExchange("/login/oauth")
    Account login(@RequestBody EmailRequest emailRequest);

    @PostExchange("/signup")
    ResponseEntity<SignupResponse> saveAccount(@RequestBody SignupRequest signupRequest);

    @GetExchange("/check-email")
    boolean isExistsEmail(@RequestParam("email") String email, @RequestParam("isSocial") boolean isSocial);

    @PostExchange("/signup/oauth")
    ResponseEntity<SignupResponse> saveAccountWithOauth(@RequestBody SignupOauthRequest signupOauthRequest);

    @PostExchange("/activate")
    void activate(@RequestBody EmailRequest emailRequest);

}
