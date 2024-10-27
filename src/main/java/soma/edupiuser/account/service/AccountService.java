package soma.edupiuser.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
<<<<<<< HEAD
import soma.edupiuser.web.exception.InnerServerException;
import soma.edupiuser.web.exception.MetaValidException;
import soma.edupiuser.web.models.ErrorResponse;
=======
import soma.edupiuser.web.exception.ErrorEnum;
>>>>>>> 85e1c7b ([#48]feat: 토큰 만료 예외, 예외 구조 추가)

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final MetaServerApiClient metaServerApiClient;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    public String login(AccountLoginRequest accountLoginRequest) {
        Account findAccount = metaServerApiClient.login(accountLoginRequest);

        return tokenProvider.generateToken(findAccount);
    }

    public ResponseEntity<SignupResponse> signup(SignupRequest signupRequest) throws JsonProcessingException {
        try {
            // 회원가입 요청을 처리
            ResponseEntity<SignupResponse> responseEntity = metaServerApiClient.saveAccount(signupRequest);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseEntity.getBody());

        } catch (HttpClientErrorException e) {
<<<<<<< HEAD
            handleHttpClientException(e);
        }
        // 헝성 예외를 던지기 때문에 이 부분은 도달하지 않음
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private void handleHttpClientException(HttpClientErrorException e) throws JsonProcessingException {
        // 예외의 응답 바디를 읽어 Response 객체로 변환
        ErrorResponse response = objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);

        if (e.getStatusCode().is4xxClientError()) {
            throw new MetaValidException(response.getMessage());

        } else if (e.getStatusCode().is5xxServerError()) {
            throw new InnerServerException(response.getMessage());

        } else {
            throw new InnerServerException(e.getMessage());
=======
            log.error("signup exception {}", e.getResponseBodyAsString());
            throw new MetaServerException(ErrorEnum.META_SERVER_EXCEPTION);
>>>>>>> 85e1c7b ([#48]feat: 토큰 만료 예외, 예외 구조 추가)
        }
    }

    public TokenInfo findAccountInfo(String token) {
        return tokenProvider.findAccountInfo(token);
    }

}
