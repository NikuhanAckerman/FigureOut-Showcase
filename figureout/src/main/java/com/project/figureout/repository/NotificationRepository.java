package com.project.figureout.repository;

import com.project.figureout.model.Client;
import com.project.figureout.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    public List<Notification> findAllByClientId(Long clientId);
}
