package com.nhom6.server.Model;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietBaiThiId implements Serializable {
    private String maKetQua;
    private String maCauHoi;
}
