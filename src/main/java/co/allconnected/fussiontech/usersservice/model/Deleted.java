package co.allconnected.fussiontech.usersservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "deleted", schema = "all_connected_users")
public class Deleted {
    public Deleted(String idUser, String reason) {
        this.idUser = idUser;
        this.reason = reason;
        this.deleteDate = Instant.now();
    }

    @Id
    @Column(name = "id_user", nullable = false, length = 50)
    private String idUser;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "reason", nullable = false, length = 200)
    private String reason;

    @Column(name = "delete_date", nullable = false)
    private Instant deleteDate;
}