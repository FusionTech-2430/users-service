package co.allconnected.fussiontech.usersservice.repository;

import co.allconnected.fussiontech.usersservice.model.Deleted;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedRepository extends JpaRepository<Deleted, String> {
}