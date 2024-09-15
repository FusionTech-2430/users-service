package co.allconnected.fussiontech.usersservice.dtos;

import co.allconnected.fussiontech.usersservice.model.Rol;
import co.allconnected.fussiontech.usersservice.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private String id_user;
    private String fullname;
    private String username;
    private String mail;
    private String photo_url;
    private String[] roles;
    private String[] organizations;
    private boolean active;

    public UserDTO(User user){
        this.id_user = user.getIdUser();
        this.fullname = user.getFullname();
        this.username = user.getUsername();
        this.mail = user.getMail();
        this.photo_url = user.getPhotoUrl();
        this.roles = user.getRoles().stream().map(Rol::getIdRol).toArray(String[]::new);
        // this.organizations = user.getOrganizations().stream().map(Organization::getIdOrganization).toArray(String[]::new);
        this.active = user.getActive();
    }
}
