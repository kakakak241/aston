package danila.zubov.aston.repository;

import danila.zubov.aston.entity.Account;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

  private final List<Account> accounts;

  @Override
  public Optional<Account> findAccountByUuid(UUID uuid) {
    return accounts.stream().filter(account -> account.getUuid().equals(uuid)).findFirst();
  }

  @Override
  public Account getAccountByUuid(UUID uuid) {
    return findAccountByUuid(uuid).orElseThrow(
        () -> new NoSuchElementException("Account not found with UUID: " + uuid));
  }

  @Override
  public List<Account> findAll() {
    return new ArrayList<>(accounts);
  }

  @Override
  public Account save(Account account) {
    var existingAccount = findAccountByUuid(account.getUuid());

    if (existingAccount.isPresent()) {
      Account oldAccount = existingAccount.get();
      account.setNumber(oldAccount.getNumber());
      account.setUuid(oldAccount.getUuid());
      accounts.remove(oldAccount);
      accounts.add(account);
    } else {
      account.setNumber(rand(0, 99999999));
      account.setUuid(UUID.randomUUID());
      accounts.add(account);
    }

    return account;
  }

  @Override
  public void delete(Account account) {
    accounts.removeIf(existingUser -> existingUser.getUuid().equals(account.getUuid()));
  }

  private int rand(int start, int end) {
    Random random = new Random();
    int range = end - start + 1;

    return start + (int) (random.nextFloat() * range);
  }
}
