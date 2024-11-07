package soma.edupiuser.account.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import soma.edupiuser.account.service.domain.AccountRole;

@Data
@Builder
@AllArgsConstructor
public class TokenInfo {

    private String email;
    private String name;
    private String provider;
    private AccountRole accountRole;
}
