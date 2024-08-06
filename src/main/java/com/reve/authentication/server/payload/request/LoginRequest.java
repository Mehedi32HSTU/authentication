package com.reve.authentication.server.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "Can't be blank.")
    private String username;

    @NotBlank(message = "Can't be blank.")
    private String password;

    private boolean isRememberMeClicked;


}
