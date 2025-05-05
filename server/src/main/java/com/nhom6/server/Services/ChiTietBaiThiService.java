package com.nhom6.server.Services;

import com.nhom6.server.DTO.AnswerDto;
import com.nhom6.server.DTO.ChiTietBaiThiDto;
import com.nhom6.server.DTO.SaveAnswerDto;
import com.nhom6.server.Model.*;
import com.nhom6.server.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChiTietBaiThiService {
    @Autowired
    private ChiTietBaiThiRepository chiTietBaiThiRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired// nếu cần
    private AnswerRepository answerRepository;

    // ✅ Trả về danh sách câu hỏi và đáp án đã làm của 1 kết quả
    public List<ChiTietBaiThiDto> getChiTietBaiLam(String maketqua) {
        List<ChiTietBaiThi> chiTietList = chiTietBaiThiRepository.findByKetQua_Maketqua(maketqua);
        Map<String, ChiTietBaiThiDto> map = new LinkedHashMap<>();

        for (ChiTietBaiThi chiTiet : chiTietList) {
            Question cauHoi = chiTiet.getCauHoi();
            String maCauHoi = cauHoi.getMacauhoi();

            map.putIfAbsent(maCauHoi, new ChiTietBaiThiDto(
                    maCauHoi,
                    cauHoi.getNoidung(),
                    chiTiet.getThutu(),
                    new ArrayList<>()
            ));

            ChiTietBaiThiDto dto = map.get(maCauHoi);
            List<Answer> listDapAn = cauHoi.getCauTraLoiList();

            for (Answer dapAn : listDapAn) {
                boolean laDapAnChon = chiTiet.getDapAnChon() != null &&
                        chiTiet.getDapAnChon().getMacautraloi().equals(dapAn.getMacautraloi());
                dto.getDapAns().add(new AnswerDto(
                        dapAn.getMacautraloi(),
                        dapAn.getNoidung(),
                        dapAn.isLadapan(),
                        laDapAnChon
                ));
            }
        }

        return new ArrayList<>(map.values());
    }

    // ✅ Lấy danh sách mã câu hỏi đã có trong chi tiết đề
    public List<String> checkCauHoi(String maKetQua) {
        List<ChiTietBaiThi> list = chiTietBaiThiRepository.findByKetQua_Maketqua(maKetQua);
        List<String> maCauHoiList = new ArrayList<>();
        for (ChiTietBaiThi ctb : list) {
            maCauHoiList.add(ctb.getCauHoi().getMacauhoi());
        }
        return maCauHoiList;
    }

    // ✅ Random câu hỏi và lưu vào chi tiết đề
    @Transactional
    public List<Question> randomCauHoi(String maKetQua, String maMonHoc, int soCau) {
        List<Question> danhSach = questionRepository.findRandomByMonHoc(maMonHoc, soCau);
        Result ketQua = resultRepository.findById(maKetQua).orElseThrow();

        int index = 1;
        List<ChiTietBaiThi> result = new ArrayList<>();
        for (Question q : danhSach) {
            ChiTietBaiThi chiTiet = new ChiTietBaiThi();
            chiTiet.setKetQua(ketQua);
            chiTiet.setCauHoi(q);
            chiTiet.setThutu(index++);
            chiTiet.setDapAnChon(null);
            result.add(chiTiet);
        }

        chiTietBaiThiRepository.saveAll(result);
        return danhSach;
    }

    // ✅ Trả về danh sách câu hỏi theo danh sách đã random (dạng DTO)
    public List<ChiTietBaiThiDto> getCauHoi(List<Question> cauHoiList, String maKetQua) {
        List<ChiTietBaiThiDto> result = new ArrayList<>();

        Map<String, String> mapDapAnChon = new HashMap<>();
        chiTietBaiThiRepository.findByKetQua_Maketqua(maKetQua).forEach(ct -> {
            if (ct.getDapAnChon() != null) {
                mapDapAnChon.put(ct.getCauHoi().getMacauhoi(), ct.getDapAnChon().getMacautraloi());
            }
        });

        for (Question q : cauHoiList) {
            ChiTietBaiThiDto dto = new ChiTietBaiThiDto();
            dto.setMaCauHoi(q.getMacauhoi());
            dto.setNoiDungCauHoi(q.getNoidung());
            dto.setThuTu(0); // nếu cần thì truyền thêm từ ngoài vào

            List<AnswerDto> dapAns = new ArrayList<>();
            for (Answer a : q.getCauTraLoiList()) {
                boolean laChon = a.getMacautraloi().equals(mapDapAnChon.get(q.getMacauhoi()));
                dapAns.add(new AnswerDto(
                        a.getMacautraloi(),
                        a.getNoidung(),
                        a.isLadapan(),
                        laChon
                ));
            }
            dto.setDapAns(dapAns);
            result.add(dto);
        }

        return result;
    }

    // ✅ Lưu đáp án được chọn
    @Transactional
    public void saveAnswer(SaveAnswerDto answer) {
        ChiTietBaiThiId id = new ChiTietBaiThiId(answer.getMaketqua(), answer.getMacauhoi());
        ChiTietBaiThi chiTiet = chiTietBaiThiRepository.findById(id).orElseThrow();
        Answer dapAn = answerRepository.findById(answer.getDapanchon()).orElse(null);
        chiTiet.setDapAnChon(dapAn);
        chiTietBaiThiRepository.save(chiTiet);
    }
}