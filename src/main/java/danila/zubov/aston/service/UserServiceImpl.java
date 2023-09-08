package danila.zubov.aston.service;

import danila.zubov.aston.dto.UserCreateDto;
import danila.zubov.aston.dto.UserUpdateDto;
import danila.zubov.aston.dto.UserViewDto;
import danila.zubov.aston.mapper.UserMapper;
import danila.zubov.aston.repository.AccountRepository;
import danila.zubov.aston.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository repository;

  private final AccountRepository accountRepository;

  private final UserMapper mapper;

  @Override
  public UserViewDto create(UserCreateDto dto) {
    return mapper.toViewDto(repository.save(mapper.toEntity(dto)));
  }

  @Override
  public void delete(UUID uuid) {
    var user = repository.getUserByUuid(uuid);
    user.getAccounts().forEach(accountRepository::delete);
    repository.delete(user);
  }

  @Override
  public UserViewDto getOne(UUID uuid) {
    return mapper.toViewDto(repository.getUserByUuid(uuid));
  }

  @Override
  public List<UserViewDto> getList() {
    return repository.findAll().stream().map(mapper::toViewDto).toList();
  }

  @Override
  public UserViewDto update(UUID uuid, UserUpdateDto dto) {
    var user = repository.getUserByUuid(uuid);
    mapper.updateUser(user, dto);
    repository.save(user);
    return mapper.toViewDto(user);
  }
}
