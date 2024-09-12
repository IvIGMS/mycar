package com.mycar.business.repositories;

import com.mycar.business.entities.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<IssueEntity, Long> {
}
