package com.nhom6.server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<CauHoi, String> {
    // Tìm câu hỏi theo môn học và trạng thái
    List<CauHoi> findByMamonhocAndTrangthai(String mamonhoc, boolean trangthai);
    
    // Tìm câu hỏi theo trạng thái
    List<CauHoi> findByTrangthai(boolean trangthai);
    
    // Tìm câu hỏi có mã lớn nhất (sắp xếp giảm dần và lấy đầu tiên)
    CauHoi findTopByOrderByMacauhoiDesc();
    
}