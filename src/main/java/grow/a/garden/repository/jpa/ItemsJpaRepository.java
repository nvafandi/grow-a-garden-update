package grow.a.garden.repository.jpa;

import grow.a.garden.entity.ItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemsJpaRepository extends JpaRepository<ItemsEntity, UUID> {
}
