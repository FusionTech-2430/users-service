package co.allconnected.fussiontech.usersservice.repository;

import co.allconnected.fussiontech.usersservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}