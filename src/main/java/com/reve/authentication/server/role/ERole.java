package com.reve.authentication.server.role;

public enum ERole {
    ROLE_USER("ROLE_USER"),
    ROLE_MODERATOR("ROLE_MODERATOR"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN");


    private final String role;
    private ERole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
    public static ERole fromString(String role) {
        for (ERole eRole : ERole.values()) {
            if (eRole.getRole().equalsIgnoreCase(role)) {
                return eRole;
            }
        }
        throw new IllegalArgumentException("No enum constant for role: " + role);
    }
}
