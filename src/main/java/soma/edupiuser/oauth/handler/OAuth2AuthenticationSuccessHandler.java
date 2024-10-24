package soma.edupiuser.oauth.handler;

import static soma.edupiuser.oauth.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME;
import static soma.edupiuser.oauth.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import soma.edupiuser.account.auth.TokenProvider;
import soma.edupiuser.account.client.DbServerApiClient;
import soma.edupiuser.account.models.EmailRequest;
import soma.edupiuser.account.service.domain.Account;
import soma.edupiuser.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import soma.edupiuser.oauth.models.OAuth2UserUnlinkManager;
import soma.edupiuser.oauth.models.SignupOauthRequest;
import soma.edupiuser.oauth.service.OAuth2UserPrincipal;
import soma.edupiuser.oauth.utils.CookieUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final DbServerApiClient dbServerApiClient;
    private final TokenProvider tokenProvider;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        log.info("=== OAuth2 Success Handler 시작 ===");
        try {
            Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

            String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
            log.info("redirect url : " + targetUrl);

            String accessToken = determineTargetUrl(request, response, authentication);

            if (response.isCommitted()) {
                logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
                return;
            }

            // 토큰을 쿠키에 추가
            Cookie cookie = new Cookie("token", accessToken);
            cookie.setHttpOnly(true); // JavaScript에서 접근 불가
            cookie.setPath("/"); // 모든 경로에서 접근 가능
            cookie.setMaxAge(60 * 60); // 1시간 유효

            response.addCookie(cookie);

            clearAuthenticationAttributes(request, response);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } catch (Exception e) {
            log.error("OAuth2 Success Handler 에러 발생", e);
            throw e;
        }

    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {

        String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
            .map(Cookie::getValue)
            .orElse("");

        OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

        String email = principal.getUserInfo().getEmail();
        String name = principal.getUserInfo().getName();

        if (!dbServerApiClient.isExistsEmail(email)) {
            // 회원가입
            dbServerApiClient.saveAccountWithOauth(
                SignupOauthRequest.builder()
                    .email(email)
                    .name(name)
                    .build()
            );
        }
        // 로그인
        Account account = dbServerApiClient.login(new EmailRequest(email));

        return tokenProvider.generateToken(account);

//        else if ("unlink".equalsIgnoreCase(mode)) {
//
//            String accessToken = principal.getUserInfo().getAccessToken();
//            OAuth2Provider provider = principal.getUserInfo().getProvider();
//
//            // TODO: DB 삭제
//            // TODO: 리프레시 토큰 삭제
//            oAuth2UserUnlinkManager.unlink(provider, accessToken);
//
//            return UriComponentsBuilder.fromUriString(targetUrl)
//                .build().toUriString();
//        }
//
//        return UriComponentsBuilder.fromUriString(targetUrl)
//            .queryParam("error", "Login failed")
//            .build().toUriString();
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}