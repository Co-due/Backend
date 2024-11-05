package soma.edupiuser.oauth.models;

import java.util.Map;

public class NaverOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final String email;
    private final String name;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        // attributes 맵의 response 키의 값에 실제 attributes 맵이 할당되어 있음
        this.attributes = (Map<String, Object>) attributes.get("response");
        this.email = (String) this.attributes.get("email");
        this.name = (String) this.attributes.get("name");
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.NAVER;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }
}
