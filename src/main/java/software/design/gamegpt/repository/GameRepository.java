package software.design.gamegpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import software.design.gamegpt.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
