package com.muky.toto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PredefineListRepository extends JpaRepository<PredefineListEntity, Integer> {
    
    /**
     * Find a predefine list by draw number and game type.
     * Uses the unique constraint on (draw_number, game_type).
     */
    Optional<PredefineListEntity> findByDrawNumberAndGameType(Integer drawNumber, Integer gameType);
}

