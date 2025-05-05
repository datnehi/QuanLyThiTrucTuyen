package com.nhom6.server.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chitietde")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ChiTietBaiThiId.class)
public class ChiTietBaiThi {

    @Id
    @Column(name = "maketqua", length = 10)
    private String maKetQua;

    @Id
    @Column(name = "macauhoi", length = 10)
    private String maCauHoi;

    @Column(name = "dapanchon", length = 10)
    private String dapAnChon;

    @Column(name = "thutu")
    private Integer thuTu;
}
