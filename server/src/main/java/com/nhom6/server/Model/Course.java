package com.nhom6.server.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "monhoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @Column(name = "mamonhoc", length = 5)
    private String maMonHoc;

    @Column(name = "tenmonhoc", nullable = false, length = 100)
    private String tenMonHoc;

    @Column(name = "giangvien", length = 100)
    private String giangVien;

    @Column(name = "sotinchi")
    private Integer soTinChi;

    @Column(name = "ghichu", length = 255)
    private String ghiChu;

    @Column(name = "trangthai")
    private boolean trangThai = false;
}
