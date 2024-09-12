package soma.edupi.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import soma.edupi.user.domain.Role;

@Data
@Builder
@AllArgsConstructor
public class TokenInfo {

    private String email;
    private String name;
    private Role role;
}
