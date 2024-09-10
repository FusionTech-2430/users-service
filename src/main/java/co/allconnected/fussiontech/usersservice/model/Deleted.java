package co.allconnected.fussiontech.usersservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "deleted", schema = "all_connected_users")
public class Deleted {
    @Id
    @Column(name = "id_user", nullable = false, length = 28)
    private String idUser;

    @Column(name = "reason", nullable = false, length = 200)
    private String reason;

    @Column(name = "delete_date", nullable = false)
    private Instant deleteDate;

}