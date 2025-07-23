package grow.a.garden.repository.jpa;

import grow.a.garden.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsersJpaRepository extends JpaRepository<UsersEntity, UUID> {


}
