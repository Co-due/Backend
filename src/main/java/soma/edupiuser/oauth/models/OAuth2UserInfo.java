package soma.edupiuser.oauth.models;

import java.util.Map;

public interface OAuth2UserInfo {

    OAuth2Provider getProvider();

    Map<String, Object> getAttributes();

    String getEmail();

    String getName();
}