package com.mycar.business.controllers.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Builder
public class IssueQueryDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime notificationDate;
    private Integer notificationDistance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String typeName;
    private Long statusId;
    private String statusName;
    private Long typeId;

    public IssueQueryDTO(Long id, String name, String description, LocalDateTime notificationDate,
                         Integer notificationDistance, LocalDateTime createdAt, LocalDateTime updatedAt,
                         String typeName, Long statusId, String statusName, Long typeId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.notificationDate = notificationDate;
        this.notificationDistance = notificationDistance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.typeName = typeName;
        this.statusId = statusId;
        this.statusName = statusName;
        this.typeId = typeId;
    }
}
