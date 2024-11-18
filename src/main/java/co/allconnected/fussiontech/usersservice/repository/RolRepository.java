package co.allconnected.fussiontech.usersservice.repository;

import co.allconnected.fussiontech.usersservice.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, String> {
}