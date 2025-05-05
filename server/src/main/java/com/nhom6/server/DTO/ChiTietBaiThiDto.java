package com.nhom6.server.DTO;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietBaiThiDto {
    private String maCauHoi;
    private String noiDungCauHoi;
    private int thuTu;
    private List<AnswerDto> dapAns = new ArrayList<>();
}

