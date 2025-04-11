package com.nhom6.server.Services;

import com.nhom6.server.Model.CauHoi;
import com.nhom6.server.Model.CauTraLoi;

import java.util.List;

public interface QuestionService {
    List<CauHoi> getAllQuestions();

    List<CauHoi> getQuestionsBySubject(String mamonhoc);

    CauHoi createQuestion(CauHoi cauhoi);

    void deleteQuestion(String macauhoi);

    void saveAnswers(List<CauTraLoi> answers);

    // chỉnh sửa câu hỏi
    // Thêm các phương thức sau vào interface QuestionService
    List<CauTraLoi> getAnswersByQuestionId(String macauhoi);

    CauHoi updateQuestion(String macauhoi, CauHoi cauhoi);

    void updateAnswers(String macauhoi, List<CauTraLoi> answers);
}