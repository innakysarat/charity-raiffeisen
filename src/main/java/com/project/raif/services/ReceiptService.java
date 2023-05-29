package com.project.raif.services;

import com.project.raif.email.EmailDetails;
import com.project.raif.models.dto.Item;
import com.project.raif.models.dto.ReceiptRequestDto;
import com.project.raif.models.dto.ReceiptResponseDto;
import com.project.raif.models.entity.Client;
import com.project.raif.models.entity.Fund;
import com.project.raif.models.entity.Qr;
import com.project.raif.repositories.QrRepository;
import com.project.raif.services.clients.RaifClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ReceiptService {
    private final RaifClientService raifClientService;
    private final QrRepository qrRepository;

    public boolean preprocessingQr(String qrId) {
        Qr qr = qrRepository.findByQrId(qrId).get(0);
        return qr.getFund().getIsCharity();
    }

    public ReceiptResponseDto createAndRegisterReceipt(String email, String qrId) {
        List<Qr> qrs = qrRepository.findByQrId(qrId);
        Fund fund = qrs.get(0).getFund();
        String receiptNumber = UUID.randomUUID().toString();
        ReceiptRequestDto apiRequest = ReceiptRequestDto.builder()
                .receiptNumber(receiptNumber)
                .client(new Client(email))
                .items(new ArrayList<>(List.of(new Item(fund.getTitle(), qrs.get(0).getAmount(),
                        BigDecimal.ONE, qrs.get(0).getAmount(), fund.getVatType()))))
                .total(qrs.get(0).getAmount())
                .build();
        qrs.get(0).setReceiptNumber(receiptNumber);
        qrs.get(1).setReceiptNumber(receiptNumber);
        qrRepository.save(qrs.get(0));
        qrRepository.save(qrs.get(1));
        ReceiptResponseDto response = raifClientService.createReceipt(apiRequest);
        return raifClientService.registerReceipt(response.getReceiptNumber());
    }

    public EmailDetails createCharityReceipt(String email, String qrId) {
        Qr qr = qrRepository.findByQrId(qrId).get(0);
        String message = String.format("Пожертвование %.2f₽ в благотворительную организацию %s прошло успешно. Спасибо!",
                qr.getAmount(), qr.getFund().getTitle());
        return EmailDetails.builder()
                .recipient(email)
                .msgBody(message)
                .subject("Пожертвование")
                .build();
    }

    public String getReceipt(String qrId) {
        Qr qr = qrRepository.findByQrId(qrId).get(0);
        return (qr.getReceiptNumber() != null) ? qr.getReceiptNumber() : "No receipt";
    }
}
