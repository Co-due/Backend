package soma.edupi.user.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import soma.edupi.user.domain.Account;
import soma.edupi.user.dto.request.AccountLoginRequest;
import soma.edupi.user.dto.request.EmailRequest;
import soma.edupi.user.dto.request.SignupRequest;
import soma.edupi.user.dto.response.SignupResponse;

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
