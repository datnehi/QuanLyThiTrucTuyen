package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "phanmon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(PhanMonId.class)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.IntSequenceGenerator.class,
        property = "@id"
)
public class PhanMon {
    @Id
    @ManyToOne
    @JoinColumn(name = "id")
    private User nguoiDung;

    @Id
    @ManyToOne
    @JoinColumn(name = "mamonhoc")
    private Course monHoc;

    private boolean trangthai = false;
}
