package danila.zubov.aston.service;

import danila.zubov.aston.dto.AccountCreateDto;
import danila.zubov.aston.dto.AccountUpdateDto;
import danila.zubov.aston.dto.AccountViewDto;
import danila.zubov.aston.dto.TransactionDepositDto;
import danila.zubov.aston.dto.TransactionTransferDto;
import danila.zubov.aston.dto.TransactionWithdrawDto;
import java.util.List;
import java.util.UUID;

public interface AccountService {

  AccountViewDto create(UUID userUuid, AccountCreateDto dto);

  void delete(UUID userUuid, UUID uuid);

  List<AccountViewDto> getAll();

  AccountViewDto getOne(UUID accountUuid);

  List<AccountViewDto> getUserAccounts(UUID userUuid);

  AccountViewDto update(UUID accountUuid, AccountUpdateDto dto);

  void deposit(TransactionDepositDto dto);

  void transfer(TransactionTransferDto dto);

  void withdraw(TransactionWithdrawDto dto);

}
