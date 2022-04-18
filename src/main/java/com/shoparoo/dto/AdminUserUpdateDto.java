package com.shoparoo.dto;

import com.shoparoo.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AdminUserUpdateDto extends UserUpdateDto {
    @NotNull(message = "{user.enabled.invalid}")
    private Boolean enabled;

    @NotEmpty(message = "{user.role.invalid}")
    private Set<Role> roles = new HashSet<>();
}
