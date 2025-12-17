package com.cloud.cloudnotes.user.dto;

import com.cloud.cloudnotes.user.UserEntity;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public static UserEntity toEntity(RegisterDto dto) {
        if (dto == null) return null;
        UserEntity entity = new UserEntity();
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        return entity;
    }

    public static RegisterDto toDto(UserEntity entity) {
        if (entity == null) return null;
        return new RegisterDto(entity.getUsername(), entity.getPassword());
    }

    public static UserEntity toEntityLogin (LoginDto dto) {
        if (dto == null) return null;
        UserEntity entity = new UserEntity();
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        return entity;
    }

}
