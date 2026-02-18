package com.project.figureout.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notificacoes")
@Getter @Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "not_id")
    private long id;

    //fetch = FetchType.EAGER
    @ManyToOne
    @JoinColumn(name = "not_cli_id")
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "not_cat")
    private NotificationCategoryEnum category;

    @Column(name = "not_datahora")
    private LocalDateTime notificationDateTime;

    @Column(name = "not_titulo")
    private String title;

    @Column(name = "not_descricao")
    private String description;

}
