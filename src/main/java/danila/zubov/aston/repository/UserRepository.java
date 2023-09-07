package danila.zubov.aston.repository;

import danila.zubov.aston.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  Optional<User> findUserByUuid(UUID uuid);

  User getUserByUuid(UUID uuid);

  List<User> findAll();


  User save(User user);

  void delete(User user);
}
