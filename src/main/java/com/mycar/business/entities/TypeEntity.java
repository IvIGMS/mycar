package com.mycar.business.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "types")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50, name = "type_name")
    private String typeName;

    @OneToMany(mappedBy = "typeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueEntity> relatedEntities;
}
