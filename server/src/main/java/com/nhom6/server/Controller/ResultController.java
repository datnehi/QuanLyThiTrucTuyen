package com.nhom6.server.Controller;

import com.nhom6.server.Model.Result;
import com.nhom6.server.Services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultController {
    @Autowired
    private ResultService resultService;

    @GetMapping
    public ResponseEntity<List<Result>> getAllExams() {
        List<Result> results = resultService.getAllResults();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{maKiThi}")
    public ResponseEntity<List<Result>> getExamById(@PathVariable String maKiThi) {
            List<Result> result = resultService.getResultById(maKiThi);
            return ResponseEntity.ok(result);

    }
}
