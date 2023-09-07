package danila.zubov.aston.service;

import danila.zubov.aston.dto.UserCreateDto;
import danila.zubov.aston.dto.UserUpdateDto;
import danila.zubov.aston.dto.UserViewDto;
import java.util.List;
import java.util.UUID;

public interface UserService {

  UserViewDto create(UserCreateDto dto);

  void delete(UUID uuid);

  UserViewDto getOne(UUID uuid);

  List<UserViewDto> getList();

  UserViewDto update(UUID uuid, UserUpdateDto dto);

}
