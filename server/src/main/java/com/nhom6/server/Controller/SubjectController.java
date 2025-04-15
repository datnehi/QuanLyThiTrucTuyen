package com.nhom6.server.Controller;

import com.nhom6.server.Model.Subject;
import com.nhom6.server.Services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Subject>> getActiveSubjects() {
        List<Subject> subjects = subjectService.getActiveSubjects();
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        Subject createdSubject = subjectService.createSubject(subject);
        return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
    }

    @PutMapping("/{mamonhoc}")
    public ResponseEntity<Subject> updateSubject(@PathVariable String mamonhoc, @RequestBody Subject subject) {
        Subject updatedSubject = subjectService.updateSubject(mamonhoc, subject);
        return new ResponseEntity<>(updatedSubject, HttpStatus.OK);
    }

    @DeleteMapping("/{mamonhoc}")
    public ResponseEntity<Void> deleteSubject(@PathVariable String mamonhoc) {
        subjectService.deleteSubject(mamonhoc);
        return new ResponseEntity<>(HttpStatus.OK); // Trả về 200 thay vì 204
    }

    @PatchMapping("/{mamonhoc}/toggle-status")
    public ResponseEntity<Void> toggleSubjectStatus(@PathVariable String mamonhoc) {
        subjectService.toggleSubjectStatus(mamonhoc);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Subject>> searchSubjects(@RequestParam String keyword) {
        List<Subject> subjects = subjectService.searchSubjects(keyword);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }
}