package soma.edupiuser.web.auth;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import soma.edupiuser.account.models.TokenInfo;
import soma.edupiuser.account.service.domain.Account;
import soma.edupiuser.account.service.domain.AccountRole;

@SpringBootTest
public class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    private Account account;

    @BeforeEach
    void init() {
        account = new Account("asdf@naver.com", "홍길동", AccountRole.USER);
    }

    @Test
    @DisplayName("회원 정보를 받고 토큰 생성 확인")
    void generateToken() {
        String token = tokenProvider.generateToken(account);

        Assertions.assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("토큰 정보를 받고 회원 정보 반환")
    void findUserInfoBy() {
        String token = tokenProvider.generateToken(account);

        TokenInfo tokenInfo = tokenProvider.findAccountInfo(token);

        Assertions.assertThat(tokenInfo.getEmail()).isEqualTo(account.getEmail());
        Assertions.assertThat(tokenInfo.getName()).isEqualTo(account.getName());
        Assertions.assertThat(tokenInfo.getAccountRole()).isEqualTo(account.getAccountRole());
    }
}
