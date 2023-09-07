package danila.zubov.aston.entity;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

  @NonNull
  @EqualsAndHashCode.Include
  private UUID uuid;

  @NonNull
  private String name;

  private List<Account> accounts;

}
