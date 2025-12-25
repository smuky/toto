package com.muky.toto.controllers;

import com.muky.toto.controllers.response.PermissionResponse;
import com.muky.toto.repositories.UserPermissionEntity;
import com.muky.toto.repositories.UserPermissionRepository;
import com.muky.toto.security.PermissionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final UserPermissionRepository userPermissionRepository;

    @GetMapping
    public ResponseEntity<PermissionResponse> getUserPermission() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // This is the UID you set in the authentication object

        PermissionEnum permission = determineUserPermission(userId);
        return ResponseEntity.ok(PermissionResponse.of(userId, permission));
    }

    private PermissionEnum determineUserPermission(String userId) {
        if (userId == null || userId.isEmpty()) {
            return PermissionEnum.FREE;
        }

        // Check if user has a specific permission in the database
        Optional<UserPermissionEntity> userPermission = userPermissionRepository.findById(userId);
        
        if (userPermission.isPresent()) {
            return userPermission.get().getPermissionLevel();
        }
        
        return PermissionEnum.FREE;
    }
}
