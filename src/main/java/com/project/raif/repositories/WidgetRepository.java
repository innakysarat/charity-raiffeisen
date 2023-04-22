package com.project.raif.repositories;

import com.project.raif.models.entity.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public interface WidgetRepository extends JpaRepository<Widget, Long> {
}
