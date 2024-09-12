package com.mycar.business.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "status")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50, name = "status_name")
    private String statusName;

    @OneToMany(mappedBy = "statusEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueEntity> relatedEntities;
}
