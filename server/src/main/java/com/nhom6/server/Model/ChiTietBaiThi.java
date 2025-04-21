package com.nhom6.server.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "chitietde")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ChiTietBaiThiId.class)
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