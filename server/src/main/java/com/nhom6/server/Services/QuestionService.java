package com.nhom6.server.Services;

import com.nhom6.server.Model.Answer;
import com.nhom6.server.Model.Question;
import com.nhom6.server.Repository.AnswerRepository;
import com.nhom6.server.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public List<Question> getRandomQuestions(String maMonHoc, int soCau) {
        return questionRepository.findRandomQuestions(maMonHoc, soCau);
    }

    public List<Question> getQuestionsByMaCauHoiList(List<String> maCauHoiList) {
        return questionRepository.findByMaCauHoiIn(maCauHoiList);
    }

    public Optional<Question> getQuestionByMaCauHoi(String maCauHoi) {
        return questionRepository.findByMaCauHoi(maCauHoi);
    }

    public int countQuestionsByMaMonHoc(String maMonHoc) {
            return questionRepository.countByMaMonHoc(maMonHoc);
    }

    public List<Question> getAllQuestions() {
        // Chỉ lấy những câu hỏi có trạng thái = 0 (chưa xóa)
        return questionRepository.findByTrangThai(false);
    }

    public List<Question> getQuestionsBySubject(String mamonhoc) {
        // Chỉ lấy những câu hỏi có trạng thái = 0 (chưa xóa) và thuộc môn học
        return questionRepository.findByMaMonHocAndTrangThai(mamonhoc, false);
    }

    public Question createQuestion(Question cauhoi) {
        // Đảm bảo trạng thái mặc định là false (0)
        cauhoi.setTrangThai(false);
        if (cauhoi.getMaCauHoi() == null || cauhoi.getMaCauHoi().isEmpty()) {
            // Lấy câu hỏi cuối cùng
            Question lastQuestion = questionRepository.findTopByOrderByMaCauHoiDesc();
            if (lastQuestion != null) {
                try {
                    // Tăng số thứ tự lên 1
                    int lastNumber = Integer.parseInt(lastQuestion.getMaCauHoi());
                    String newMacauhoi = String.format("%010d", lastNumber + 1);
                    cauhoi.setMaCauHoi(newMacauhoi);
                } catch (NumberFormatException e) {
                    // Nếu không phải số, tạo UUID
                    String uniqueID = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
                    cauhoi.setMaCauHoi(uniqueID);
                }
            } else {
                // Nếu không có câu hỏi nào, bắt đầu từ 0000000001
                cauhoi.setMaCauHoi("0000000001");
            }
        }
        return questionRepository.save(cauhoi);
    }

    public void deleteQuestion(String macauhoi) {
        // Thay vì xóa, chúng ta sẽ cập nhật trạng thái thành true (1)
        questionRepository.findById(macauhoi).ifPresent(question -> {
            question.setTrangThai(true);
            questionRepository.save(question);
        });
    }

    public void saveAnswers(List<Answer> answers) {
        if (answers == null || answers.isEmpty())
            return;

        // Lấy câu trả lời cuối cùng
        Answer lastAnswer = answerRepository.findTopByOrderByMaCauTraLoiDesc();
        int lastNumber = 0;

        if (lastAnswer != null) {
            try {
                lastNumber = Integer.parseInt(lastAnswer.getMaCauTraLoi());
            } catch (NumberFormatException e) {
                // Nếu không phải số, bắt đầu từ 1
                lastNumber = 0;
            }
        }

        for (Answer answer : answers) {
            if (answer.getMaCauTraLoi() == null || answer.getMaCauTraLoi().isEmpty()) {
                lastNumber++;
                String newMacautraloi = String.format("%010d", lastNumber);
                answer.setMaCauTraLoi(newMacautraloi);
            }
        }

        answerRepository.saveAll(answers);
    }

    // chỉnh sửa câu hỏi
    public List<Answer> getAnswersByQuestionId(String macauhoi) {
        return answerRepository.findByMaCauHoi(macauhoi);
    }

    public Question updateQuestion(String macauhoi, Question cauhoi) {
        cauhoi.setMaCauHoi(macauhoi);
        return questionRepository.save(cauhoi);
    }

    public void updateAnswers(String macauhoi, List<Answer> answers) {
        // Lấy số lớn nhất hiện có trong DB
        Answer lastAnswer = answerRepository.findTopByOrderByMaCauTraLoiDesc();
        int lastNumber = 0;
        if (lastAnswer != null) {
            try {
                lastNumber = Integer.parseInt(lastAnswer.getMaCauTraLoi());
            } catch (NumberFormatException e) {
                lastNumber = 0;
            }
        }

        for (Answer answer : answers) {
            if (answer.getMaCauTraLoi() == null || answer.getMaCauTraLoi().isEmpty()) {
                // Tạo mã mới cho câu trả lời chưa có mã
                lastNumber++;
                String newMaCauTraLoi = String.format("%010d", lastNumber);
                answer.setMaCauTraLoi(newMaCauTraLoi);
            }
            // Gán lại mã câu hỏi
            answer.setMaCauHoi(macauhoi);
        }

        // Lưu tất cả (update nếu mã có rồi, insert nếu mã mới)
        answerRepository.saveAll(answers);
    }
}