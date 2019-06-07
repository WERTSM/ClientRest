package dto;

import enumeration.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class UserDTO implements Serializable {

    private String id;

    private String login;

    private String hashPassword;

    private Role role;
}