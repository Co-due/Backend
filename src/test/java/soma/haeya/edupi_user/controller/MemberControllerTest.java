package soma.haeya.edupi_user.controller;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import soma.haeya.edupi_user.dto.request.MemberLoginRequest;
import soma.haeya.edupi_user.service.MemberService;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

  @MockBean
  MemberService memberService;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("로그인에 성공하면 jwt 토큰을 쿠키에 넣는다.")
  void login() throws Exception {
    MemberLoginRequest memberLoginRequest = MemberLoginRequest.builder()
        .email("asdf@naver.com")
        .password("asdf1234")
        .build();

    doReturn("token").when(memberService).login(memberLoginRequest);

    mockMvc.perform(post("/member/login")
            .content(objectMapper.writeValueAsString(memberLoginRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andExpect(cookie().exists("token"));

  }

}