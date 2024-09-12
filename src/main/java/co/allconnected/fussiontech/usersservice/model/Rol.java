package co.allconnected.fussiontech.usersservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "rol", schema = "all_connected_users")
public class Rol {
    @Id
    @Column(name = "id_rol", nullable = false, length = Integer.MAX_VALUE)
    private String idRol;

    @JsonIgnore
    @ManyToMany(mappedBy = "rols")
    private Set<User> users = new LinkedHashSet<>();

}