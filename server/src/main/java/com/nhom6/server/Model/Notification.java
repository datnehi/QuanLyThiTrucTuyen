package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "thongbao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mathongbao")
public class Notification {
    @Id
    @Column(length = 10)
    private String mathongbao;

    @Column(nullable = false, length = 255)
    private String noidung;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoigiantao;

    private boolean trangthai = false;

    @OneToMany(mappedBy = "thongBao")
    private List<DetailNotification> chiTietThongBaos;
}