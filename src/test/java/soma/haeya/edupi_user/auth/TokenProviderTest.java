package soma.haeya.edupi_user.auth;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import soma.haeya.edupi_user.domain.Member;
import soma.haeya.edupi_user.domain.Role;
import soma.haeya.edupi_user.dto.TokenInfo;

@SpringBootTest
public class TokenProviderTest {

  @Autowired
  private TokenProvider tokenProvider;

  private Member member;

  @BeforeEach
  void init() {
    member = new Member("asdf@naver.com", "홍길동", Role.ROLE_USER);
  }

  @Test
  @DisplayName("회원 정보를 받고 토큰 생성 확인")
  void generateToken() {
    String token = tokenProvider.generateToken(member);

    Assertions.assertThat(token).isNotNull();
  }

  @Test
  @DisplayName("토큰 정보를 받고 회원 정보 반환")
  void findUserInfoBy() {
    String token = tokenProvider.generateToken(member);

    TokenInfo tokenInfo = tokenProvider.findUserInfoBy(token);

    Assertions.assertThat(tokenInfo.getEmail()).isEqualTo(member.getEmail());
    Assertions.assertThat(tokenInfo.getName()).isEqualTo(member.getName());
    Assertions.assertThat(tokenInfo.getRole()).isEqualTo(member.getRole());
  }
}
