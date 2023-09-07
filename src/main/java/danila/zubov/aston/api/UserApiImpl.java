package danila.zubov.aston.api;

import danila.zubov.aston.dto.UserCreateDto;
import danila.zubov.aston.dto.UserUpdateDto;
import danila.zubov.aston.dto.UserViewDto;
import danila.zubov.aston.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserApiImpl implements UserApiDelegate {

  private final UserService service;

  @Override
  public ResponseEntity<UserViewDto> createUser(UserCreateDto userCreateDto) {
    return ResponseEntity.ok(service.create(userCreateDto));
  }

  @Override
  public ResponseEntity<Void> deleteUser(UUID userUuid) {
    service.delete(userUuid);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<UserViewDto> getUser(UUID userUuid) {
    return ResponseEntity.ok(service.getOne(userUuid));
  }

  @Override
  public ResponseEntity<List<UserViewDto>> getUsers() {
    return ResponseEntity.ok(service.getList());
  }

  @Override
  public ResponseEntity<UserViewDto> updateUser(UUID userUuid, UserUpdateDto userUpdateDto) {
    return ResponseEntity.ok(service.update(userUuid, userUpdateDto));
  }
}
