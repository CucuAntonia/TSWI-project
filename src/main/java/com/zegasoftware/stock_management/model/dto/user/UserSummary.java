package com.zegasoftware.stock_management.model.dto.user;

import com.zegasoftware.stock_management.model.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSummary {

    private String username;

    private UserRoles role;

//    private String sector;

}
