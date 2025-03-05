package devblackholemax.easychattingroom.dao;

import devblackholemax.easychattingroom.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByUsername(String username);
}