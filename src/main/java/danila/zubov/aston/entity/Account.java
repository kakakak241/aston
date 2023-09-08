package danila.zubov.aston.entity;

import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

  @EqualsAndHashCode.Include
  private UUID uuid;

  @EqualsAndHashCode.Include
  private User user;

  private String name;

  private Integer number;

  private Double balance = 0.0;

  private String pin;

}
