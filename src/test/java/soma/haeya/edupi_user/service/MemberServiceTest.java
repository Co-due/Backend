package soma.haeya.edupi_user.service;

import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soma.haeya.edupi_user.auth.TokenProvider;
import soma.haeya.edupi_user.client.MemberApiClient;
import soma.haeya.edupi_user.domain.Member;
import soma.haeya.edupi_user.domain.Role;
import soma.haeya.edupi_user.dto.request.MemberLoginRequest;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

  @InjectMocks
  private MemberService memberService;

  @Mock
  private MemberApiClient memberRepository;

  @Mock
  private TokenProvider tokenProvider;

  private MemberLoginRequest memberLoginRequest;

  @BeforeEach
  void init() {
    memberLoginRequest = MemberLoginRequest.builder()
        .email("asdf@naver.com")
        .password("asdf1234")
        .build();
  }


  @Test
  @DisplayName("아이디와 패스워드에 맞는 멤버가 있으면 token을 반환한다.")
  void memberLogin() {

    Member expectedMember = new Member("asdf@naver.com", "홍길동", Role.ROLE_USER);

    String expectedToken = "token";

    when(memberRepository.findMemberByEmailAndPassword(memberLoginRequest)).thenReturn(
        expectedMember);
    when(tokenProvider.generateToken(expectedMember)).thenReturn(expectedToken);

    String resultToken = memberService.login(memberLoginRequest);

    Assertions.assertThat(resultToken).isEqualTo(expectedToken);
  }

  @Test
  @DisplayName("아이디 패스워드에 맞는 멤버가 없으면 예외를 반환한다.")
  void memberLoginException() {

    when(memberRepository.findMemberByEmailAndPassword(memberLoginRequest)).thenThrow(
        new IllegalArgumentException("아이디 비밀번호가 일치하지 않습니다.")
    );

    Assertions.assertThatThrownBy(() -> memberService.login(memberLoginRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("아이디 비밀번호가 일치하지 않습니다.");
  }

}
