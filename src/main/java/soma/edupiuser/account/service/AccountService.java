package soma.edupiuser.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import soma.edupiuser.account.exception.MetaServerException;
import soma.edupiuser.account.models.AccountLoginRequest;
import soma.edupiuser.account.models.SignupRequest;
import soma.edupiuser.account.models.SignupResponse;
import soma.edupiuser.account.models.TokenInfo;
import soma.edupiuser.account.service.domain.Account;
import soma.edupiuser.web.auth.TokenProvider;
import soma.edupiuser.web.client.MetaServerApiClient;
import soma.edupiuser.web.exception.ErrorEnum;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final MetaServerApiClient metaServerApiClient;
    private final TokenProvider tokenProvider;

    public String login(AccountLoginRequest accountLoginRequest) {
        try {
            Account findAccount = metaServerApiClient.login(accountLoginRequest);
            log.info("login success email={}", accountLoginRequest.getEmail());
            return tokenProvider.generateToken(findAccount);

        } catch (HttpClientErrorException e) {
            log.error("signup exception {}", e.getResponseBodyAsString());
            throw new MetaServerException(ErrorEnum.INVALID_ACCOUNT);
        }
    }

    public ResponseEntity<SignupResponse> signup(SignupRequest signupRequest) {
        try {
            ResponseEntity<SignupResponse> responseEntity = metaServerApiClient.saveAccount(signupRequest);
            log.info("signup success email={}, name={}", signupRequest.getEmail(), signupRequest.getName());
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseEntity.getBody());

        } catch (HttpClientErrorException e) {
            log.error("signup exception {}", e.getResponseBodyAsString());
            throw new MetaServerException(ErrorEnum.META_SERVER_EXCEPTION);
        }
    }

    public TokenInfo findAccountInfo(String token) {
        return tokenProvider.findAccountInfo(token);
    }

}
