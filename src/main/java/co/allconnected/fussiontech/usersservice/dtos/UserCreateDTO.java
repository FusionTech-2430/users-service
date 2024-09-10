package co.allconnected.fussiontech.usersservice.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateDTO {
    private String fullname;
    private String username;
    private String mail;
    private String photoUrl;
    private BigDecimal locationLat;
    private BigDecimal locationLng;
    private Boolean active;
}