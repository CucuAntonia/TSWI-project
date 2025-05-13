package com.zegasoftware.stock_management.mapper;

import com.zegasoftware.stock_management.model.dto.user.UserDetails;
import com.zegasoftware.stock_management.model.dto.user.UserSummary;
import com.zegasoftware.stock_management.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserSummary userSummary);
    User toEntity(UserDetails userDetails);
}
