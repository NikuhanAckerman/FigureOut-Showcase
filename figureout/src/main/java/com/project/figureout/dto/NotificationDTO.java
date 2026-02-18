package com.project.figureout.dto;

import com.project.figureout.model.NotificationCategoryEnum;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NotificationDTO {

    private NotificationCategoryEnum category;

    private String title;

    private String description;

}
