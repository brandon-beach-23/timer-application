package com.brandonbeach.timer_application.repository;

import com.brandonbeach.timer_application.model.TimerSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerSessionRepository extends JpaRepository<TimerSession, Long>{

}


