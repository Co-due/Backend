package soma.haeya.edupi_user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import soma.haeya.edupi_user.auth.TokenProvider;
import soma.haeya.edupi_user.client.MemberApiClient;
import soma.haeya.edupi_user.domain.Member;
import soma.haeya.edupi_user.dto.TokenInfo;
import soma.haeya.edupi_user.dto.request.MemberLoginRequest;
import soma.haeya.edupi_user.dto.request.SignupRequest;
import soma.haeya.edupi_user.dto.response.ErrorResponse;
import soma.haeya.edupi_user.dto.response.SignupResponse;
import soma.haeya.edupi_user.exception.DbValidException;
import soma.haeya.edupi_user.exception.InnerServerException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberApiClient memberApiClient;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    public String login(MemberLoginRequest memberLoginRequest) {
        Member findMember = memberApiClient.findMemberByEmailAndPassword(memberLoginRequest);

        return tokenProvider.generateToken(findMember);
    }

    public ResponseEntity<SignupResponse> signUp(SignupRequest signupRequest) throws JsonProcessingException {
        try {
            // 회원가입 요청을 처리
            ResponseEntity<SignupResponse> responseEntity = memberApiClient.saveMember(signupRequest);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseEntity.getBody());

        } catch (HttpClientErrorException e) {
            handleHttpClientException(e);
        }
        // 헝성 예외를 던지기 때문에 이 부분은 도달하지 않음
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public boolean checkEmailDuplication(String email) {
        //중복 이메일 확인
        return Boolean.TRUE.equals(memberApiClient.isEmailDuplicated(email).getBody());
    }

    private void handleHttpClientException(HttpClientErrorException e) throws JsonProcessingException {
        // 예외의 응답 바디를 읽어 Response 객체로 변환
        ErrorResponse response = objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);

        if (e.getStatusCode().is4xxClientError()) {
            throw new DbValidException(response.getMessage());

        } else if (e.getStatusCode().is5xxServerError()) {
            throw new InnerServerException(response.getMessage());

        } else {
            throw new InnerServerException(e.getMessage());
        }
    }

    public TokenInfo findMemberInfo(String token) {
        return tokenProvider.findUserInfoBy(token);
    }

}
