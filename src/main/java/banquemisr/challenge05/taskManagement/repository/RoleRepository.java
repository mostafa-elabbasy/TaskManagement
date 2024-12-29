package banquemisr.challenge05.taskManagement.repository;

import banquemisr.challenge05.taskManagement.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Set<Role> findByAuthorityIn(List<String> authorities);

}
