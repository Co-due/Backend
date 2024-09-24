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
    private Role role;

    public Account(Long id, String email, String password, String name, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Account(String email, String name, Role role) {
        this.email = email;
        this.name = name;
        this.role = role;
    }

}
