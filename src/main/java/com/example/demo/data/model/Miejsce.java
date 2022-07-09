package com.example.demo.data.model;

import com.example.demo.data.service.InitService;
import lombok.*;
import javax.annotation.Nullable;
import javax.persistence.*;

@Data
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Miejsce {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idMiejsce;
    @Enumerated(EnumType.ORDINAL)
    private InitService.Location location;
    @Enumerated(EnumType.ORDINAL)
    private InitService.Status status;
    @Nullable
    @ManyToOne
    @JoinColumn(name = "id_zamowienie")
    private Zamowienie idZamowienia;

    public Zamowienie getId() {
        return idZamowienia;
    }

    public void setId(Zamowienie idZamowienia) {
        this.idZamowienia = idZamowienia;
    }

    public Miejsce(Long idMiejsce, InitService.Location location, InitService.Status status) {
        this.idMiejsce = idMiejsce;
        this.location = location;
        this.status = status;
    }
    public InitService.Status getStatus() {
        return status;
    }

    public void setStatus(InitService.Status status) {
        this.status = status;
    }

    public Long getIdMiejsce() {
        return idMiejsce;
    }

    public InitService.Location getLocation() {
        return location;
    }
}
