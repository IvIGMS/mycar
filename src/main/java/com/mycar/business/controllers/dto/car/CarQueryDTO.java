package com.mycar.business.controllers.dto.car;

import com.mycar.business.entities.IssueEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CarQueryDTO {
    private Long id;
    private String companyName;
    private String modelName;
    private Long userId;
    private Integer km;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<IssueEntity> issues;

    public CarQueryDTO(Long id, String companyName, String modelName, Long userId, Integer km, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.companyName = companyName;
        this.modelName = modelName;
        this.userId = userId;
        this.km = km;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
