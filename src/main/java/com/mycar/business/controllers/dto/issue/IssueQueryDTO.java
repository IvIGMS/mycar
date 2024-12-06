package com.mycar.business.controllers.dto.issue;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
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
    private Long carId;
}
