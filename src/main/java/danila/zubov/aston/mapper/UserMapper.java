package danila.zubov.aston.mapper;

import danila.zubov.aston.dto.UserCreateDto;
import danila.zubov.aston.dto.UserUpdateDto;
import danila.zubov.aston.dto.UserViewDto;
import danila.zubov.aston.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User toEntity(UserCreateDto userCreateDto);

  UserViewDto toViewDto(User user);

  void updateUser(@MappingTarget User user, UserUpdateDto userUpdateDto);
  
}
