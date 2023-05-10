package com.project.raif.models.entity;

import com.project.raif.models.enums.VatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Fund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    private String login;
    private String password;
    private String title;
    private Boolean isCharity = true;
    private VatType vatType = VatType.VAT0;
    @OneToMany(mappedBy = "fund", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<Widget> widgets = new HashSet<>();
    @OneToMany(mappedBy = "fund", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<Qr> qrs = new HashSet<>();

    public void addWidget(Widget widget) {
        widgets.add(widget);
    }

    public void removeWidget(Widget widget) {
        widgets.remove(widget);
    }

    public void addQr(Qr qr) {qrs.add(qr);}

    public void removeQr(Qr qr) {
        qrs.remove(qr);
    }

}
