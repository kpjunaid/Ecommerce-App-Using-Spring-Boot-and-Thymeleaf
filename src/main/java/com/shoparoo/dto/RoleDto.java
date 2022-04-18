package com.shoparoo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {
    private Long id;

    @NotEmpty(message = "{role.title.invalid}")
    @Size(max = 64, message = "{role.title.invalid}")
    private String title;

    @Size(max = 1024, message = "{role.description.invalid}")
    private String description;
}
