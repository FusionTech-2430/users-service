package co.allconnected.fussiontech.usersservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "\"user\"", schema = "all_connected_users")
public class User {
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
    @ManyToMany(mappedBy = "users")
    private Set<Rol> rols = new LinkedHashSet<>();

}