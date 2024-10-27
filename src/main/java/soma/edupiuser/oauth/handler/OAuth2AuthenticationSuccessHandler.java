package soma.edupiuser.oauth.handler;

import static soma.edupiuser.oauth.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME;
import static soma.edupiuser.oauth.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import soma.edupiuser.account.models.EmailRequest;
import soma.edupiuser.account.service.domain.Account;
import soma.edupiuser.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import soma.edupiuser.oauth.models.OAuth2Provider;
import soma.edupiuser.oauth.models.OAuth2UserUnlinkManager;
import soma.edupiuser.oauth.models.SignupOauthRequest;
import soma.edupiuser.oauth.service.OAuth2UserPrincipal;
import soma.edupiuser.oauth.utils.CookieUtils;
import soma.edupiuser.web.auth.TokenProvider;
import soma.edupiuser.web.client.MetaServerApiClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MetaServerApiClient dbServerApiClient;
    private final TokenProvider tokenProvider;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        try {
            String targetUrl = determineTargetUrl(request);
            OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

            if (principal == null) {
                String errorUrl = getErrorUrl(response, targetUrl, "Login failed");

                clearAuthenticationAttributes(request, response);
                getRedirectStrategy().sendRedirect(request, response, errorUrl);
                return;
            }

            handleMode(request, response, principal);

            if (response.isCommitted()) {
                logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
                return;
            }

            clearAuthenticationAttributes(request, response);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } catch (Exception e) {
            log.error("OAuth2 Success Handler 에러 발생", e);
            throw e;
        }

    }

    private String determineTargetUrl(HttpServletRequest request) {
        return CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
            .map(Cookie::getValue)
            .orElse(getDefaultTargetUrl());
    }

    private String getErrorUrl(HttpServletResponse response, String targetUrl, String errorMessage) {
        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("error", errorMessage)
            .build().toUriString();
    }

    private void handleMode(HttpServletRequest request, HttpServletResponse response, OAuth2UserPrincipal principal) {
        String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
            .map(Cookie::getValue)
            .orElse("");
        if ("login".equalsIgnoreCase(mode)) {
            login(principal, response);
        } else if ("unlink".equalsIgnoreCase(mode)) {
            unlink(principal);
        }
    }

    private void login(OAuth2UserPrincipal principal, HttpServletResponse response) {
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
        String accessToken = tokenProvider.generateToken(account);

        addAccessTokenToCookie(response, accessToken);

    }

    private void addAccessTokenToCookie(HttpServletResponse response, String accessToken) {
        // 토큰을 쿠키에 추가
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true); // JavaScript에서 접근 불가
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setMaxAge(60 * 60); // 1시간 유효
        response.addCookie(cookie);
    }

    private void unlink(OAuth2UserPrincipal principal) {
        String accessToken = principal.getUserInfo().getAccessToken();
        OAuth2Provider provider = principal.getUserInfo().getProvider();
        oAuth2UserUnlinkManager.unlink(provider, accessToken);
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