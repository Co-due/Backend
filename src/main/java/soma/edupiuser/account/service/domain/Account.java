package soma.edupiuser.account.service.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Account {

    private Long id;
    private String password;
    private String email;
    private String name;
    private String provider;
    private AccountRole accountRole;

    public Account(Long id, String email, String password, String name, String provider, AccountRole accountRole) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.provider = provider;
        this.accountRole = accountRole;
    }

    public Account(String email, String name, AccountRole accountRole) {
        this.email = email;
        this.name = name;
        this.accountRole = accountRole;
    }

}
