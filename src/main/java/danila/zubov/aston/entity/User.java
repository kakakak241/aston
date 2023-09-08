package danila.zubov.aston.entity;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

  @EqualsAndHashCode.Include
  private UUID uuid;

  private String name;

  private List<Account> accounts;

  public User(UUID uuid, String name) {
    this.uuid = uuid;
    this.name = name;
  }

}
