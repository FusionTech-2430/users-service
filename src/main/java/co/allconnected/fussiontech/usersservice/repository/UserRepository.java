package co.allconnected.fussiontech.usersservice.repository;

import co.allconnected.fussiontech.usersservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u " +
            "LEFT JOIN u.roles r " +
            "WHERE (:fullname IS NULL OR u.fullname LIKE %:fullname%) " +
            "AND (:username IS NULL OR u.username LIKE %:username%) " +
            "AND (:mail IS NULL OR u.mail LIKE %:mail%) " +
            "AND (:rol IS NULL OR r.idRol LIKE %:rol%) " +
            "AND (:active IS NULL OR u.active = :active)")
    List<User> findUsersByFilters(@Param("fullname") String fullname,
                                  @Param("username") String username,
                                  @Param("mail") String mail,
                                  @Param("rol") String rol,
                                  @Param("active") Boolean active);

}