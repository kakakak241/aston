package danila.zubov.aston.mapper;

import danila.zubov.aston.dto.AccountCreateDto;
import danila.zubov.aston.dto.AccountUpdateDto;
import danila.zubov.aston.dto.AccountViewDto;
import danila.zubov.aston.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {

  Account toEntity(AccountCreateDto createDto);

  AccountViewDto toViewDto(Account account);

  void updateAccount(@MappingTarget Account account, AccountUpdateDto updateDto);

}
