package soma.edupi.user.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Member {

    private Long id;
    private String password;
    private String email;
    private String name;
    private Role role;

    public Member(Long id, String email, String password, String name, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Member(String email, String name, Role role) {
        this.email = email;
        this.name = name;
        this.role = role;
    }

}
