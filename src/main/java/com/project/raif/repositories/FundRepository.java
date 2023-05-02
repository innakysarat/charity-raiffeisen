package com.project.raif.repositories;

import com.project.raif.models.entity.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface FundRepository extends JpaRepository<Fund, Long> {
    Optional<Fund> findByLogin(String login);

    Fund findByLoginAndTitle(String login, String title);
}
