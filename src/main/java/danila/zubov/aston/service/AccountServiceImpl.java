package danila.zubov.aston.service;

import danila.zubov.aston.dto.AccountCreateDto;
import danila.zubov.aston.dto.AccountUpdateDto;
import danila.zubov.aston.dto.AccountViewDto;
import danila.zubov.aston.dto.TransactionDepositDto;
import danila.zubov.aston.dto.TransactionTransferDto;
import danila.zubov.aston.dto.TransactionWithdrawDto;
import danila.zubov.aston.exception.InsufficientBalanceException;
import danila.zubov.aston.exception.InvalidTransactionException;
import danila.zubov.aston.mapper.AccountMapper;
import danila.zubov.aston.repository.AccountRepository;
import danila.zubov.aston.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository repository;

  private final UserRepository userRepository;

  private final AccountMapper mapper;

  @Override
  public AccountViewDto create(UUID userUuid, AccountCreateDto dto) {
    var user = userRepository.getUserByUuid(userUuid);
    var account = mapper.toEntity(dto);
    user.getAccounts().add(account);
    userRepository.save(user);
    account.setUser(user);
    return mapper.toViewDto(repository.save(account));
  }

  @Override
  public void delete(UUID userUuid, UUID uuid) {
    var user = userRepository.getUserByUuid(userUuid);
    user.getAccounts().removeIf(account -> account.getUuid().equals(uuid));
    var account = repository.getAccountByUuid(uuid);
    repository.delete(account);
  }

  @Override
  public List<AccountViewDto> getAll() {
    return repository.findAll().stream().map(mapper::toViewDto).toList();
  }

  @Override
  public AccountViewDto getOne(UUID accountUuid) {
    return mapper.toViewDto(repository.getAccountByUuid(accountUuid));
  }

  @Override
  public List<AccountViewDto> getUserAccounts(UUID userUuid) {
    return userRepository.getUserByUuid(userUuid).getAccounts().stream().map(mapper::toViewDto)
        .toList();
  }

  @Override
  public AccountViewDto update(UUID accountUuid, AccountUpdateDto dto) {
    var account = repository.getAccountByUuid(accountUuid);
    mapper.updateAccount(account, dto);
    return mapper.toViewDto(account);
  }

  @Override
  public void deposit(TransactionDepositDto dto) {
    if (dto.getAmount() <= 0) {
      throw new InvalidTransactionException("Deposit amount should be positive.");
    }
    var account = repository.getAccountByUuid(dto.getAccountUuid());

    var balance = account.getBalance();
    balance += dto.getAmount();
    account.setBalance(balance);

    repository.save(account);
  }

  @Override
  public void transfer(TransactionTransferDto dto) {
    if (dto.getAmount() <= 0) {
      throw new InvalidTransactionException("Transfer amount should be positive.");
    }

    var sourceAccount = repository.getAccountByUuid(dto.getAccountUuid());
    if (sourceAccount.getBalance() - dto.getAmount() < 0) {
      throw new InsufficientBalanceException("Insufficient balance for transfer.");
    }

    sourceAccount.setBalance(sourceAccount.getBalance() - dto.getAmount());
    repository.save(sourceAccount);

    var targetAccount = repository.getAccountByUuid(dto.getAccountTransferUuid());
    targetAccount.setBalance(targetAccount.getBalance() + dto.getAmount());
    repository.save(targetAccount);
  }

  @Override
  public void withdraw(TransactionWithdrawDto dto) {
    if (dto.getAmount() <= 0) {
      throw new InvalidTransactionException("Withdrawal amount should be positive.");
    }

    var account = repository.getAccountByUuid(dto.getAccountUuid());
    if (account.getBalance() - dto.getAmount() < 0) {
      throw new InsufficientBalanceException("Insufficient balance for withdrawal.");
    }

    account.setBalance(account.getBalance() - dto.getAmount());
    repository.save(account);
  }
}
