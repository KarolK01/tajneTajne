package com.example.demo.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
public class Zamowienie{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_zamowienia;
    private String name;
    private String lastName;
    private String email;
    private int phoneNumber;
    private int persons;
    private boolean nextToWindow;
    @JoinColumn(name="idMiejsce")
    private String idMiejsce;

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

}