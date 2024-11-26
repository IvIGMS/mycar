package com.mycar.business.controllers.dto;

import com.mycar.business.entities.TypeEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IssueCreateDTO {
    @NotNull(message = "El campo 'name' es obligatorio.")
    private String name;
    private String description;
    private Integer notificationDateDays;
    private Integer notificationDistance;
    private Integer currentDistance;
    @NotNull(message = "El campo 'type' es obligatorio.")
    private Long typeId;
}
