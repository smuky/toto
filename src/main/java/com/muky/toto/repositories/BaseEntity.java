package com.muky.toto.repositories;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp // Automatically sets on first insert
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp // Automatically refreshes on every update
    private LocalDateTime updatedAt;
}