package com.nhom6.server.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDto {
    private String maCauTraLoi;
    private String noiDung;
    private boolean laDapAn;
    private boolean laDapAnChon;
}
