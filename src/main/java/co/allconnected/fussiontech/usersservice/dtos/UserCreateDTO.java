package co.allconnected.fussiontech.usersservice.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateDTO {
    private String fullname;
    private String username;
    private String password;
    private String mail;
    private String[] roles;
}