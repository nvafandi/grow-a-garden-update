package grow.a.garden.repository.jpa;

import grow.a.garden.entity.WishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WishJpaRespository extends JpaRepository<WishEntity, UUID> {

    WishEntity findByWishId(String wishId);

    List<WishEntity> findByUserId(String userId);

}
