package com.ms.AUTH_SERVICE.repo;

import com.ms.AUTH_SERVICE.model.AdminActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminActionLogRepository extends JpaRepository<AdminActionLog, Long> {
}
