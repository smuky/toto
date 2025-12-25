package com.muky.toto.repositories;

import com.muky.toto.security.PermissionEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "user_permissions")
public class UserPermissionEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_level", nullable = false, length = 20)
    private PermissionEnum permissionLevel;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

}