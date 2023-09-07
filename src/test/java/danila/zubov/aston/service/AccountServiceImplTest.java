package danila.zubov.aston.service;

import danila.zubov.aston.dto.AccountCreateDto;
import danila.zubov.aston.dto.AccountUpdateDto;
import danila.zubov.aston.dto.AccountViewDto;
import danila.zubov.aston.dto.TransactionDepositDto;
import danila.zubov.aston.dto.TransactionTransferDto;
import danila.zubov.aston.dto.TransactionWithdrawDto;
import danila.zubov.aston.entity.Account;
import danila.zubov.aston.entity.User;
import danila.zubov.aston.exception.InsufficientBalanceException;
import danila.zubov.aston.exception.InvalidTransactionException;
import danila.zubov.aston.mapper.AccountMapper;
import danila.zubov.aston.repository.AccountRepository;
import danila.zubov.aston.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

class AccountServiceImplTest {

  @InjectMocks
  private AccountServiceImpl accountService;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private AccountMapper accountMapper;

  private UUID userUuid;
  private UUID accountUuid;
  private Account account;
  private User user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    userUuid = UUID.randomUUID();
    accountUuid = UUID.randomUUID();

    user = new User(userUuid, "test");
    account = new Account();
    account.setUuid(accountUuid);

    user.setAccounts(new ArrayList<>());
  }

  @Test
  void shouldCreateAccountSuccessfully() {
    AccountCreateDto dto = new AccountCreateDto("1234");
    dto.setName("Simple");
    when(userRepository.getUserByUuid(userUuid)).thenReturn(user);
    when(accountMapper.toEntity(dto)).thenReturn(account);
    when(accountRepository.save(account)).thenReturn(account);
    when(accountRepository.save(account)).thenReturn(account);

    AccountViewDto result = accountService.create(userUuid, dto);

    assertNotNull(result);
    verify(accountRepository).save(account);
  }

  @Test
  void shouldDeleteAccountSuccessfully() {
    user.getAccounts().add(account);

    when(userRepository.getUserByUuid(userUuid)).thenReturn(user);
    when(accountRepository.getAccountByUuid(accountUuid)).thenReturn(account);

    accountService.delete(userUuid, accountUuid);

    assertFalse(user.getAccounts().contains(account));
    verify(accountRepository).delete(account);
  }

  @Test
  void shouldGetAllAccountsSuccessfully() {
    when(accountRepository.findAll()).thenReturn(Arrays.asList(account));

    List<AccountViewDto> results = accountService.getAll();

    assertEquals(1, results.size());
  }

  @Test
  void shouldGetOneAccountSuccessfully() {
    when(accountRepository.getAccountByUuid(accountUuid)).thenReturn(account);

    AccountViewDto result = accountService.getOne(accountUuid);

    assertNotNull(result);
  }

  @Test
  void shouldUpdateAccountSuccessfully() {
    AccountUpdateDto dto = new AccountUpdateDto("testUpdate");
    when(accountRepository.getAccountByUuid(accountUuid)).thenReturn(account);

    AccountViewDto result = accountService.update(accountUuid, dto);

    assertNotNull(result);
    verify(accountMapper).updateAccount(account, dto);
  }

  @Test
  void shouldDepositSuccessfully() {
    double initialBalance = 100.0;
    double depositAmount = 50.0;

    account.setBalance(initialBalance);

    TransactionDepositDto dto = new TransactionDepositDto(userUuid, accountUuid, depositAmount);

    when(accountRepository.getAccountByUuid(accountUuid)).thenReturn(account);

    accountService.deposit(dto);

    assertEquals(initialBalance + depositAmount, account.getBalance());
  }

  @Test
  void shouldThrowInvalidTransactionExceptionOnNegativeDeposit() {
    TransactionDepositDto dto = new TransactionDepositDto(userUuid, accountUuid, -50.0);
    assertThrows(InvalidTransactionException.class, () -> accountService.deposit(dto));
  }

  @Test
  void shouldTransferSuccessfully() {
    double initialBalance = 100.0;
    double transferAmount = 50.0f;

    Account targetAccount = new Account();
    targetAccount.setBalance(0d);

    account.setBalance(initialBalance);

    TransactionTransferDto dto = new TransactionTransferDto(UUID.randomUUID(), "1234", userUuid, accountUuid, transferAmount);

    when(accountRepository.getAccountByUuid(accountUuid)).thenReturn(account);
    when(accountRepository.getAccountByUuid(dto.getAccountTransferUuid())).thenReturn(targetAccount);

    accountService.transfer(dto);

    assertEquals(initialBalance - transferAmount, account.getBalance());
    assertEquals(transferAmount, targetAccount.getBalance());
  }

  @Test
  void shouldWithdrawSuccessfully() {
    double initialBalance = 100.0;
    double withdrawAmount = 50.0;

    account.setBalance(initialBalance);

    TransactionWithdrawDto dto = new TransactionWithdrawDto("1234", userUuid, accountUuid, withdrawAmount);

    when(accountRepository.getAccountByUuid(accountUuid)).thenReturn(account);

    accountService.withdraw(dto);

    assertEquals(initialBalance - withdrawAmount, account.getBalance());
  }

  @Test
  void shouldThrowInsufficientBalanceExceptionOnExcessiveWithdrawal() {
    double initialBalance = 50.0;
    double withdrawAmount = 100.0;

    account.setBalance(initialBalance);

    TransactionWithdrawDto dto = new TransactionWithdrawDto("1234", userUuid, accountUuid, withdrawAmount);

    when(accountRepository.getAccountByUuid(accountUuid)).thenReturn(account);

    assertThrows(InsufficientBalanceException.class, () -> accountService.withdraw(dto));
  }
}