package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chitietde")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ChiTietBaiThiId.class)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@id"
)
public class ChiTietBaiThi {
    @Id
    @ManyToOne
    @JoinColumn(name = "maketqua")
    private Result ketQua;

    @Id
    @ManyToOne
    @JoinColumn(name = "macauhoi")
    private Question cauHoi;

    @ManyToOne
    @JoinColumn(name = "dapanchon")
    private Answer dapAnChon;

    private Integer thutu;
}