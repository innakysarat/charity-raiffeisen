package com.project.raif.repositories;

import com.project.raif.models.entity.Fund;
import com.project.raif.models.entity.Qr;
import com.project.raif.models.enums.QrStatus;
import com.project.raif.models.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface QrRepository extends JpaRepository<Qr, Long> {
    //@Query(value = "select * from qr q where q.qr_status = ?1", nativeQuery = true)
    List<Qr> findAllByQrStatus(QrStatus status);

    List<Qr> findByQrId(String qrId);

    List<Qr> findBySubscriptionId(String subscriptionId);

    List<Qr> findAllBySubscriptionStatus(SubscriptionStatus subscriptionStatus);

    List<Qr> findByFund(Fund fund);
}
