package com.reve.authentication.server.securityImpl.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessToken implements Serializable {
    private String token;
    private String username;
    private boolean isRevoked;
}
