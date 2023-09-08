package danila.zubov.aston.repository;

import danila.zubov.aston.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final List<User> users;

  @Override
  public Optional<User> findUserByUuid(UUID uuid) {
    return users.stream().filter(user -> user.getUuid().equals(uuid)).findFirst();
  }

  @Override
  public User getUserByUuid(UUID uuid) {
    return findUserByUuid(uuid)
        .orElseThrow(() -> new NoSuchElementException("User not found with UUID: " + uuid));
  }


  @Override
  public List<User> findAll() {
    return new ArrayList<>(users);
  }

  @Override
  public User save(User user) {
    var existingUser = findUserByUuid(user.getUuid());

    UUID uuid = UUID.randomUUID();

    if (existingUser.isPresent()) {
      uuid = existingUser.get().getUuid();
      users.remove(existingUser.get());
    }

    user.setUuid(uuid);
    users.add(user);
    return user;
  }

  @Override
  public void delete(User user) {
    users.removeIf(existingUser -> existingUser.getUuid().equals(user.getUuid()));
  }
}
