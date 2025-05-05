package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chitiethongtbao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DetailNotificationId.class)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@id"
)
public class DetailNotification {
    @Id
    @ManyToOne
    @JoinColumn(name = "mathongbao")
    private Notification thongBao;

    @Id
    @ManyToOne
    @JoinColumn(name = "mamonhoc")
    private Course monHoc;

    private boolean trangthai = false;
}
