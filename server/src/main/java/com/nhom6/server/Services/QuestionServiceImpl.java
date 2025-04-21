package com.nhom6.server.Services;

import com.nhom6.server.Repository.AnswerRepository;
import com.nhom6.server.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public List<CauHoi> getAllQuestions() {
        // Chỉ lấy những câu hỏi có trạng thái = 0 (chưa xóa)
        return questionRepository.findByTrangthai(false);
    }

    @Override
    public List<CauHoi> getQuestionsBySubject(String mamonhoc) {
        // Chỉ lấy những câu hỏi có trạng thái = 0 (chưa xóa) và thuộc môn học
        return questionRepository.findByMamonhocAndTrangthai(mamonhoc, false);
    }

    @Override
    public CauHoi createQuestion(CauHoi cauhoi) {
        // Đảm bảo trạng thái mặc định là false (0)
        cauhoi.setTrangthai(false);
        if (cauhoi.getMacauhoi() == null || cauhoi.getMacauhoi().isEmpty()) {
            // Lấy câu hỏi cuối cùng
            CauHoi lastQuestion = questionRepository.findTopByOrderByMacauhoiDesc();
            if (lastQuestion != null) {
                try {
                    // Tăng số thứ tự lên 1
                    int lastNumber = Integer.parseInt(lastQuestion.getMacauhoi());
                    String newMacauhoi = String.format("%010d", lastNumber + 1);
                    cauhoi.setMacauhoi(newMacauhoi);
                } catch (NumberFormatException e) {
                    // Nếu không phải số, tạo UUID
                    String uniqueID = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
                    cauhoi.setMacauhoi(uniqueID);
                }
            } else {
                // Nếu không có câu hỏi nào, bắt đầu từ 0000000001
                cauhoi.setMacauhoi("0000000001");
            }
        }
        return questionRepository.save(cauhoi);
    }

    @Override
    public void deleteQuestion(String macauhoi) {
        // Thay vì xóa, chúng ta sẽ cập nhật trạng thái thành true (1)
        questionRepository.findById(macauhoi).ifPresent(question -> {
            question.setTrangthai(true);
            questionRepository.save(question);
        });
    }

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public void saveAnswers(List<CauTraLoi> answers) {
        if (answers == null || answers.isEmpty())
            return;

        // Lấy câu trả lời cuối cùng
        CauTraLoi lastAnswer = answerRepository.findTopByOrderByMacautraloiDesc();
        int lastNumber = 0;

        if (lastAnswer != null) {
            try {
                lastNumber = Integer.parseInt(lastAnswer.getMacautraloi());
            } catch (NumberFormatException e) {
                // Nếu không phải số, bắt đầu từ 1
                lastNumber = 0;
            }
        }

        for (CauTraLoi answer : answers) {
            if (answer.getMacautraloi() == null || answer.getMacautraloi().isEmpty()) {
                lastNumber++;
                String newMacautraloi = String.format("%010d", lastNumber);
                answer.setMacautraloi(newMacautraloi);
            }
        }

        answerRepository.saveAll(answers);
    }

    // chỉnh sửa câu hỏi
    @Override
    public List<CauTraLoi> getAnswersByQuestionId(String macauhoi) {
        return answerRepository.findByMacauhoi(macauhoi);
    }

    @Override
    public CauHoi updateQuestion(String macauhoi, CauHoi cauhoi) {
        cauhoi.setMacauhoi(macauhoi);
        return questionRepository.save(cauhoi);
    }

    @Override
    public void updateAnswers(String macauhoi, List<CauTraLoi> answers) {
        // Xóa các câu trả lời cũ
        answerRepository.deleteByMacauhoi(macauhoi);

        // Lưu các câu trả lời mới
        CauTraLoi lastAnswer = answerRepository.findTopByOrderByMacautraloiDesc();
        int lastNumber = 0;

        if (lastAnswer != null) {
            try {
                lastNumber = Integer.parseInt(lastAnswer.getMacautraloi());
            } catch (NumberFormatException e) {
                lastNumber = 0;
            }
        }

        for (CauTraLoi answer : answers) {
            lastNumber++;
            String newMacautraloi = String.format("%010d", lastNumber);
            answer.setMacautraloi(newMacautraloi);
            answer.setMacauhoi(macauhoi);
        }

        answerRepository.saveAll(answers);
    }
}