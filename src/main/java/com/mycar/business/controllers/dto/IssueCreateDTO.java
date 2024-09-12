package com.mycar.business.controllers.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IssueCreateDTO {
    private String name;
    private String description;
    private LocalDateTime notificationDate;
    private Integer notificationDistance;
    private Integer currentDistance;
}
