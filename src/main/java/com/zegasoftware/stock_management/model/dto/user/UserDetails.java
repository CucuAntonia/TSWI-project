package com.zegasoftware.stock_management.model.dto.user;

import com.zegasoftware.stock_management.model.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDetails {

    private String username;

    private String password;

    private UserRoles role;
}
