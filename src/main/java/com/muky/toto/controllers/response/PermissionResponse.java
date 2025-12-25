package com.muky.toto.controllers.response;

import com.muky.toto.security.PermissionEnum;

public record PermissionResponse(
    String userId,
    PermissionEnum permission
) {
    public static PermissionResponse of(String userId, PermissionEnum permission) {
        return new PermissionResponse(userId, permission);
    }
}
