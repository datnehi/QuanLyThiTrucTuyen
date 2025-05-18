package com.nhom6.server.DTO;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietBaiThiDto {
    private String maCauHoi;
    private String noiDungCauHoi;
    private List<AnswerDto> dapAnList = new ArrayList<>();
    private int thuTu;
}

