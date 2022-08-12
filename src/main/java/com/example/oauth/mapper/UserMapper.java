package com.example.oauth.mapper;

import com.example.oauth.controller.dto.UserDTO;
import com.example.oauth.controller.dto.UserNewDTO;
import com.example.oauth.controller.dto.UserShortDTO;
import com.example.oauth.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserShortDTO toUserShortDtoFromUser(User user);
    UserNewDTO toUserNewDtoFromUser(User user);
    UserDTO toUserDtoFromUser(User user);

    User toUserFromUserNewDto(UserNewDTO newUserDTO);
    User toUserFromUserShortDto(UserShortDTO shortUserDTO);
    User toUserFromUserDto(UserDTO userDTO);
}
