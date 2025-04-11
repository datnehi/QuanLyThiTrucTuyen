package com.nhom6.server.Repository;

import com.nhom6.server.Model.CauHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<CauHoi, String> {
    // Tìm câu hỏi theo môn học
    List<CauHoi> findByMamonhoc(String mamonhoc);
    
    // Tìm câu hỏi có mã lớn nhất (sắp xếp giảm dần và lấy đầu tiên)
    CauHoi findTopByOrderByMacauhoiDesc();
}