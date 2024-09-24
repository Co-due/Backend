package soma.edupiuser.account.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import soma.edupiuser.account.models.AccountLoginRequest;
import soma.edupiuser.account.models.EmailRequest;
import soma.edupiuser.account.models.SignupRequest;
import soma.edupiuser.account.models.SignupResponse;
import soma.edupiuser.account.service.domain.Account;

@Component
@HttpExchange("/v1/account")
public interface DbServerApiClient {

    @PostExchange("/login")
    Account login(@RequestBody AccountLoginRequest accountLoginRequest);

    @PostExchange("/signup")
    ResponseEntity<SignupResponse> saveAccount(@RequestBody SignupRequest signupRequest);

    @PostExchange("/activate")
    void activate(@RequestBody EmailRequest emailRequest);

}
