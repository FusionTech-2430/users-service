package co.allconnected.fussiontech.usersservice.model;

import co.allconnected.fussiontech.usersservice.dtos.UserCreateDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "\"user\"", schema = "all_connected_users")
public class User {

    public User(UserCreateDTO dto){
        this.fullname = dto.getFullname();
        this.username = dto.getUsername();
        this.mail = dto.getMail();
        this.active = true;
    }

    @Id
    @Column(name = "id_user", nullable = false, length = 28)
    private String idUser;

    @Column(name = "fullname", length = 100)
    private String fullname;

    @Column(name = "username", nullable = false, length = 45)
    private String username;

    @Column(name = "mail", length = 45)
    private String mail;

    @Column(name = "photo_url", length = 200)
    private String photoUrl;

    @Column(name = "location_lat", precision = 9, scale = 6)
    private BigDecimal locationLat;

    @Column(name = "location_lng", precision = 9, scale = 6)
    private BigDecimal locationLng;

    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_rol",
            schema = "all_connected_users",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private Set<Rol> rols = new LinkedHashSet<>();

}