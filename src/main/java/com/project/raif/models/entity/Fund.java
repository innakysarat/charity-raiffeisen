package com.project.raif.models.entity;

import lombok.*;

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
    @OneToMany(mappedBy = "fund", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<Widget> widgets = new HashSet<>();
    public void addWidget(Widget widget) {
        widgets.add(widget);
    }
    public void removeWidget(Widget widget) {
        widgets.remove(widget);
    }

}
