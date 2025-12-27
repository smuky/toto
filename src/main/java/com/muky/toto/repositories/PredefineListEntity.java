package com.muky.toto.repositories;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "predefine_list", 
       uniqueConstraints = @UniqueConstraint(name = "unique_draw_game", columnNames = {"draw_number", "game_type"}))
public class PredefineListEntity {

    @Id
    @Column(name = "draw_id")
    private Integer drawId;

    @Column(name = "draw_number", nullable = false)
    private Integer drawNumber;

    @Column(name = "game_type", nullable = false)
    private Integer gameType;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @Column(name = "fixture_list", columnDefinition = "TEXT")
    private String fixtureList;

    @Column(name = "failed_description", columnDefinition = "TEXT")
    private String failedDescription;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }
}

