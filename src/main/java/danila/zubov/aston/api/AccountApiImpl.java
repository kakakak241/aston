package danila.zubov.aston.api;

import danila.zubov.aston.dto.AccountCreateDto;
import danila.zubov.aston.dto.AccountUpdateDto;
import danila.zubov.aston.dto.AccountViewDto;
import danila.zubov.aston.dto.TransactionDepositDto;
import danila.zubov.aston.dto.TransactionTransferDto;
import danila.zubov.aston.dto.TransactionWithdrawDto;
import danila.zubov.aston.service.AccountService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountApiImpl implements AccountApiDelegate {

  private final AccountService service;

  @Override
  public ResponseEntity<AccountViewDto> createUserAccount(UUID userUuid,
      AccountCreateDto accountCreateDto) {
    return ResponseEntity.ok(service.create(userUuid, accountCreateDto));
  }

  @Override
  public ResponseEntity<Void> deleteUserAccount(UUID userUuid, UUID accountUuid) {
    service.delete(userUuid, accountUuid);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> depositMoney(TransactionDepositDto transactionDepositDto) {
    service.deposit(transactionDepositDto);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<List<AccountViewDto>> getAllAccounts() {
    return ResponseEntity.ok(service.getAll());
  }

  @Override
  public ResponseEntity<AccountViewDto> getUserAccountByUuid(UUID userUuid, UUID accountUuid) {
    return ResponseEntity.ok(service.getOne(accountUuid));
  }

  @Override
  public ResponseEntity<List<AccountViewDto>> getUserAccounts(UUID userUuid) {
    return ResponseEntity.ok(service.getUserAccounts(userUuid));
  }

  @Override
  public ResponseEntity<Void> transferMoney(TransactionTransferDto transactionTransferDto) {
    service.transfer(transactionTransferDto);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<AccountViewDto> updateAccount(UUID userUuid, UUID accountUuid,
      AccountUpdateDto accountUpdateDto) {
    return ResponseEntity.ok(service.update(accountUuid, accountUpdateDto));
  }

  @Override
  public ResponseEntity<Void> withdrawMoney(TransactionWithdrawDto transactionWithdrawDto) {
    service.withdraw(transactionWithdrawDto);
    return ResponseEntity.noContent().build();
  }
}
