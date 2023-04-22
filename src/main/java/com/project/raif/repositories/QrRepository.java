package com.project.raif.repositories;

import com.project.raif.models.entity.Qr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Repository
public interface QrRepository extends JpaRepository<Qr, Long> {
    @Query(value = "select * from qr q where q.qr_status = ?1", nativeQuery = true)
    List<Qr> findAllByQrStatus(String status);

    Qr findByQrId(String qrId);
}
