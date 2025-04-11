package com.nhom6.server.Controller;

import com.nhom6.server.Model.CauHoi;
import com.nhom6.server.Model.CauTraLoi;
import com.nhom6.server.Services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<CauHoi>> getAllQuestions() {
        List<CauHoi> questions = questionService.getAllQuestions();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/subject/{mamonhoc}")
    public ResponseEntity<List<CauHoi>> getQuestionsBySubject(@PathVariable String mamonhoc) {
        List<CauHoi> questions = questionService.getQuestionsBySubject(mamonhoc);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CauHoi> createQuestion(@RequestBody CauHoi cauhoi) {
        CauHoi createdQuestion = questionService.createQuestion(cauhoi);
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
    public ResponseEntity<Void> saveAnswers(@RequestBody List<CauTraLoi> answers) {
        questionService.saveAnswers(answers);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //chỉnh sửa câu hỏi
    // Thêm các phương thức sau vào QuestionController
@GetMapping("/{macauhoi}/answers")
public ResponseEntity<List<CauTraLoi>> getAnswersByQuestionId(@PathVariable String macauhoi) {
    List<CauTraLoi> answers = questionService.getAnswersByQuestionId(macauhoi);
    return new ResponseEntity<>(answers, HttpStatus.OK);
}

@PutMapping("/{macauhoi}")
public ResponseEntity<CauHoi> updateQuestion(@PathVariable String macauhoi, @RequestBody CauHoi cauhoi) {
    CauHoi updatedQuestion = questionService.updateQuestion(macauhoi, cauhoi);
    return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
}

@PutMapping("/{macauhoi}/answers")
public ResponseEntity<Void> updateAnswers(@PathVariable String macauhoi, @RequestBody List<CauTraLoi> answers) {
    questionService.updateAnswers(macauhoi, answers);
    return new ResponseEntity<>(HttpStatus.OK);
}
}