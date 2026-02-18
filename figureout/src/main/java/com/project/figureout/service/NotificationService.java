package com.project.figureout.service;

import com.project.figureout.dto.NotificationDTO;
import com.project.figureout.model.Client;
import com.project.figureout.model.Notification;
import com.project.figureout.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification getNotificationById(long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Notificação não encontrada com base no ID."));
    }

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public List<Notification> getClientNotifications(long clientId) {
        return notificationRepository.findAllByClientId(clientId);
    }

    public Client getClientByNotification(Notification notification) {
        return notification.getClient();
    }

    public void deleteNotificationById(long id) {
        notificationRepository.deleteById(id);
    }

    public void createNotification(Client client, NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setTitle(notificationDTO.getTitle());
        notification.setCategory(notificationDTO.getCategory());
        notification.setDescription(notificationDTO.getDescription());
        notification.setNotificationDateTime(LocalDateTime.now());
        notification.setClient(client);

        saveNotification(notification);
    }





}
