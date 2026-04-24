package com.example.aitravelplanner.repository;

import com.example.aitravelplanner.entity.AiLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiLogRepository extends JpaRepository<AiLog, Long> {
    List<AiLog> findTop20ByUserIdOrderByCreatedAtDesc(Long userId);
}
