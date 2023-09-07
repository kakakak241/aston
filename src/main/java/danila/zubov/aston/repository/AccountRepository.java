package danila.zubov.aston.repository;

import danila.zubov.aston.entity.Account;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

  Optional<Account> findAccountByUuid(UUID uuid);

  Account getAccountByUuid(UUID uuid);

  List<Account> findAll();

  Account save(Account account);

  void delete(Account account);

}
