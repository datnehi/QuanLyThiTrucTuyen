package com.nhom6.server.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "phanmon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PhanMonId.class)
public class PhanMon {

    @Id
    @Column(name = "id", length = 7, nullable = false)
    private String id;

    @Id
    @Column(name = "mamonhoc", length = 5, nullable = false)
    private String maMonHoc;

    @Column(name = "trangthai")
    private boolean trangThai = false;
}
