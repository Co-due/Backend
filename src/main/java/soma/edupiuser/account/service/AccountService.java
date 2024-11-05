package soma.edupiuser.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import soma.edupiuser.account.exception.DuplicatedEmailException;
import soma.edupiuser.account.exception.MetaServerException;
import soma.edupiuser.account.models.AccountLoginRequest;
import soma.edupiuser.account.models.SignupRequest;
import soma.edupiuser.account.models.SignupResponse;
import soma.edupiuser.account.models.TokenInfo;
import soma.edupiuser.account.service.domain.Account;
import soma.edupiuser.web.auth.TokenProvider;
import soma.edupiuser.web.client.MetaServerApiClient;
import soma.edupiuser.web.exception.ErrorEnum;
import soma.edupiuser.web.models.ErrorResponse;

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
            log.error("login exception {}", e.getResponseBodyAsString());
            throw new MetaServerException(ErrorEnum.INVALID_ACCOUNT);
        } catch (ResourceAccessException e) {
            throw new MetaServerException(ErrorEnum.RESOURCE_ACCESS_EXCEPTION);
        } catch (Exception e) {
            throw new MetaServerException(ErrorEnum.TASK_FAIL);
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
            ErrorResponse errorResponse = e.getResponseBodyAs(ErrorResponse.class);
            if (errorResponse == null) {
                throw new MetaServerException(ErrorEnum.RESPONSE_FORMAT_ERROR);
            }
            if (errorResponse.getCode().equals("DB-409001")) {
                throw new DuplicatedEmailException(ErrorEnum.DUPLICATE_EMAIL);
            } else {
                throw new MetaServerException(ErrorEnum.TASK_FAIL);
            }

        } catch (ResourceAccessException e) {
            throw new MetaServerException(ErrorEnum.RESOURCE_ACCESS_EXCEPTION);
        } catch (Exception e) {
            throw new MetaServerException(ErrorEnum.TASK_FAIL);
        }
    }

    public TokenInfo findAccountInfo(String token) {
        return tokenProvider.findAccountInfo(token);
    }

}
