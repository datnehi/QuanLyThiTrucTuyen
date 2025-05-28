package com.nhom6.server.Controller;

import com.nhom6.server.Model.Answer;
import com.nhom6.server.Model.Question;
import com.nhom6.server.Services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/subject/{mamonhoc}")
    public ResponseEntity<List<Question>> getQuestionsBySubject(@PathVariable String mamonhoc) {
        List<Question> questions = questionService.getQuestionsBySubject(mamonhoc);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody Question cauhoi) {
        // Đảm bảo trạng thái mặc định là false (0)
        cauhoi.setTrangThai(false);
        Question createdQuestion = questionService.createQuestion(cauhoi);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @DeleteMapping("/{macauhoi}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String macauhoi) {
        try {
            questionService.deleteQuestion(macauhoi);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/answers")
    public ResponseEntity<Void> saveAnswers(@RequestBody List<Answer> answers) {
        questionService.saveAnswers(answers);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // chỉnh sửa câu hỏi
    // Thêm các phương thức sau vào QuestionController
    @GetMapping("/{macauhoi}/answers")
    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@PathVariable String macauhoi) {
        List<Answer> answers = questionService.getAnswersByQuestionId(macauhoi);
        return new ResponseEntity<>(answers, HttpStatus.OK);
    }

    @PutMapping("/{macauhoi}")
    public ResponseEntity<Question> updateQuestion(@PathVariable String macauhoi, @RequestBody Question cauhoi) {
        Question updatedQuestion = questionService.updateQuestion(macauhoi, cauhoi);
        return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
    }

    @PutMapping("/{macauhoi}/answers")
    public ResponseEntity<Void> updateAnswers(@PathVariable String macauhoi, @RequestBody List<Answer> answers) {
        questionService.updateAnswers(macauhoi, answers);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}