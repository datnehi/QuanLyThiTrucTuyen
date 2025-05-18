package com.nhom6.server.Services;

import com.nhom6.server.DTO.AnswerDto;
import com.nhom6.server.DTO.ChiTietBaiThiDto;
import com.nhom6.server.DTO.SaveAnswerDto;
import com.nhom6.server.Model.*;
import com.nhom6.server.Repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChiTietBaiThiService {

    @Autowired
    private ChiTietBaiThiRepository chiTietBaiThiRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerRepository answerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // 1. Trả về chi tiết bài làm theo mã kết quả
    public List<ChiTietBaiThiDto> getChiTietBaiLam(String maKetQua) {
        try {
            // Lấy tất cả ChiTietBaiThi cho maKetQua
            List<ChiTietBaiThi> chiTietBaiThis = chiTietBaiThiRepository.findByMaKetQua(maKetQua);

            // Lấy các câu hỏi liên quan từ bảng cauhoi
            Map<String, ChiTietBaiThiDto> cauHoiMap = new LinkedHashMap<>();

            for (ChiTietBaiThi chiTiet : chiTietBaiThis) {
                String maCauHoi = chiTiet.getMaCauHoi();

                // Tạo mới ChiTietBaiThiDto nếu chưa có trong map
                cauHoiMap.putIfAbsent(maCauHoi, new ChiTietBaiThiDto(
                        maCauHoi,
                        "", // Nội dung câu hỏi sẽ lấy sau từ CauHoi
                        new ArrayList<>(),
                        chiTiet.getThuTu()
                ));

                // Tìm câu hỏi và lấy nội dung câu hỏi từ CauHoiRepository
                Optional<Question> cauHoiOptional = questionService.getQuestionByMaCauHoi(maCauHoi);
                if (cauHoiOptional.isPresent()) {
                    cauHoiMap.get(maCauHoi).setNoiDungCauHoi(cauHoiOptional.get().getNoiDung());
                }

                // Tìm các đáp án liên quan cho mỗi câu hỏi
                List<Answer> answers = answerRepository.findByMaCauHoi(maCauHoi);

                // Thêm các AnswerDto vào danh sách đáp án của câu hỏi
                for (Answer answer : answers) {
                    boolean laDapAnChon = chiTiet.getDapAnChon() != null && chiTiet.getDapAnChon().equals(answer.getMaCauTraLoi());
                    AnswerDto answerDto = new AnswerDto(
                            answer.getMaCauTraLoi(),
                            answer.getNoiDung(),
                            answer.isLaDapAn(),
                            laDapAnChon
                    );

                    // Thêm AnswerDto vào danh sách đáp án của câu hỏi
                    cauHoiMap.get(maCauHoi).getDapAnList().add(answerDto);
                }
            }

            // Trả về danh sách các ChiTietBaiThiDto
            return new ArrayList<>(cauHoiMap.values());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    // 2. Kiểm tra mã câu hỏi đã có trong kết quả
    public List<ChiTietBaiThi> checkCauHoi(String maKetQua) {
        try {
            return chiTietBaiThiRepository.findByMaKetQua(maKetQua);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // 3. Random câu hỏi và insert vào chi tiết đề
    public List<Question> randomCauHoi(String maKetQua, String maMonHoc, int soCau) {
        try {

            List<Question> randomQuestions = questionService.getRandomQuestions(maMonHoc, soCau);

            int thuTu = 1;
            for (Question q : randomQuestions) {
                ChiTietBaiThi chiTiet = new ChiTietBaiThi();
                chiTiet.setMaKetQua(maKetQua);
                chiTiet.setMaCauHoi(q.getMaCauHoi());
                chiTiet.setThuTu(thuTu++);
                chiTietBaiThiRepository.save(chiTiet);
            }

            return randomQuestions;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // 4. Lấy danh sách câu hỏi theo danh sách câu hỏi và mã kết quả
    public List<ChiTietBaiThiDto> getCauHoi(List<Question> cauHoiList, String maKetQua) {
        try {
            List<ChiTietBaiThiDto> danhSachCauHoi = new ArrayList<>();

            // Lấy danh sách đáp án đã chọn từ cơ sở dữ liệu
            List<Object[]> dapAnChonList = chiTietBaiThiRepository.findDapAnChonByMaKetQua(maKetQua);
            Map<String, String> dapAnChonMap = dapAnChonList.stream()
                    .filter(row -> row[0] != null && row[1] != null)
                    .collect(Collectors.toMap(
                            row -> (String) row[0],
                            row -> (String) row[1]
                    ));

            for (Question cauHoi : cauHoiList) {
                String maCauHoi = cauHoi.getMaCauHoi();
                String noiDungCauHoi = cauHoi.getNoiDung();

                // Lấy danh sách đáp án tương ứng với câu hỏi
                List<Answer> dapAnEntities = answerRepository.findByMaCauHoi(maCauHoi);
                List<AnswerDto> dapAns = dapAnEntities.stream()
                        .map(a -> new AnswerDto(
                                a.getMaCauTraLoi(),
                                a.getNoiDung(),
                                a.isLaDapAn(),
                                a.getMaCauTraLoi().equals(dapAnChonMap.get(maCauHoi))
                        ))
                        .collect(Collectors.toList());

                danhSachCauHoi.add(new ChiTietBaiThiDto(maCauHoi, noiDungCauHoi, dapAns, 0));
            }

            return danhSachCauHoi;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // 5. Lưu đáp án chọn
    public void saveAnswer(SaveAnswerDto answer) {
        ChiTietBaiThiId id = new ChiTietBaiThiId(answer.getMaketqua(), answer.getMacauhoi());
        Optional<ChiTietBaiThi> optional = chiTietBaiThiRepository.findById(id);
        if (optional.isPresent()) {
            ChiTietBaiThi chiTiet = optional.get();
            chiTiet.setDapAnChon(answer.getDapanchon());
            chiTietBaiThiRepository.save(chiTiet);
        }
    }
}
