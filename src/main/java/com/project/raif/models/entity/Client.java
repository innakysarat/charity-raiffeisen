package com.project.raif.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Table;

@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {
    public String email;
    public String name;

    public Client(String email) {
        this.email = email;
    }
}
