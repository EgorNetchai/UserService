package ru.aston.intensive.springrestuserservice.services;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.aston.intensive.common.dto.UserNotificationDto;
import ru.aston.intensive.springrestuserservice.dto.UserDto;
import ru.aston.intensive.springrestuserservice.models.UserEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-24T21:30:37+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (JetBrains s.r.o.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setName( userEntity.getName() );
        userDto.setEmail( userEntity.getEmail() );
        userDto.setAge( userEntity.getAge() );

        return userDto;
    }

    @Override
    public UserEntity toUserEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setName( userDto.getName() );
        userEntity.setEmail( userDto.getEmail() );
        userEntity.setAge( userDto.getAge() );

        return userEntity;
    }

    @Override
    public UserNotificationDto toUserNotificationDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserNotificationDto userNotificationDto = new UserNotificationDto();

        userNotificationDto.setEmail( userEntity.getEmail() );

        return userNotificationDto;
    }
}
