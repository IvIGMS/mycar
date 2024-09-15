package com.mycar.business.repositories;

import com.mycar.business.controllers.dto.IssueQueryDTO;
import com.mycar.business.entities.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<IssueEntity, Long> {

    @Query("SELECT new com.mycar.business.controllers.dto.IssueQueryDTO(i.id AS id, i.name AS name, i.description AS description, i.notificationDate AS notificationDate, i.notificationDistance AS notificationDistance, i.createdAt AS createdAt, i.updatedAt AS updatedAt, t.typeName AS typeName, i.statusEntity.id AS statusId, s.statusName AS statusName, i.typeEntity.id AS typeId) " +
            "FROM IssueEntity i " +
            "JOIN TypeEntity t ON i.typeEntity.id = t.id " +
            "JOIN StatusEntity s ON i.statusEntity.id = s.id " +
            "WHERE i.userEntity.id = :userId")
    List<IssueQueryDTO> getIssues(@Param("userId") Long userId);
}
