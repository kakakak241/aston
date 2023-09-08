package danila.zubov.aston.service;

import danila.zubov.aston.dto.UserCreateDto;
import danila.zubov.aston.dto.UserUpdateDto;
import danila.zubov.aston.dto.UserViewDto;
import danila.zubov.aston.entity.User;
import danila.zubov.aston.mapper.UserMapper;
import danila.zubov.aston.repository.AccountRepository;
import danila.zubov.aston.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.util.UUID;

class UserServiceImplTest {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private UserMapper userMapper;

  private UUID uuid;
  private User userEntity;
  private UserViewDto viewDto;
  private UserCreateDto createDto;
  private UserUpdateDto updateDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    uuid = UUID.randomUUID();
    userEntity = new User(uuid, "test");
    viewDto = new UserViewDto(uuid, "test");
    createDto = new UserCreateDto("test");
    updateDto = new UserUpdateDto("testUpdated");
  }

  @Test
  void shouldCreateUser() {
    when(userMapper.toEntity(createDto)).thenReturn(userEntity);
    when(userMapper.toViewDto(userEntity)).thenReturn(viewDto);
    when(userRepository.save(userEntity)).thenReturn(userEntity);

    userService.create(createDto);

    verify(userRepository).save(userEntity);
    verify(userMapper).toViewDto(userEntity);
  }

  @Test
  void shouldDeleteUser() {
    when(userRepository.getUserByUuid(uuid)).thenReturn(userEntity);

    userService.delete(uuid);

    verify(userRepository).delete(userEntity);
  }

  @Test
  void shouldGetOne() {
    when(userRepository.getUserByUuid(uuid)).thenReturn(userEntity);
    when(userMapper.toViewDto(userEntity)).thenReturn(viewDto);

    userService.getOne(uuid);

    verify(userRepository).getUserByUuid(uuid);
    verify(userMapper).toViewDto(userEntity);
  }

  @Test
  void shouldGetList() {
    when(userRepository.findAll()).thenReturn(List.of(userEntity));
    when(userMapper.toViewDto(userEntity)).thenReturn(viewDto);

    userService.getList();

    verify(userRepository).findAll();
    verify(userMapper).toViewDto(userEntity);
  }

  @Test
  void shouldUpdateUser() {
    when(userRepository.getUserByUuid(uuid)).thenReturn(userEntity);
    when(userMapper.toViewDto(userEntity)).thenReturn(viewDto);

    userService.update(uuid, updateDto);

    verify(userRepository).getUserByUuid(uuid);
    verify(userMapper).updateUser(userEntity, updateDto);
    verify(userRepository).save(userEntity);
  }
}