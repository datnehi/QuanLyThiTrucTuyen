package com.nhom6.server.Repository;

import com.nhom6.server.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
//    // Tìm câu hỏi theo môn học và trạng thái
//    List<CauHoi> findByMamonhocAndTrangthai(String mamonhoc, boolean trangthai);
//
//    // Tìm câu hỏi theo trạng thái
//    List<CauHoi> findByTrangthai(boolean trangthai);
//
//    // Tìm câu hỏi có mã lớn nhất (sắp xếp giảm dần và lấy đầu tiên)
//    CauHoi findTopByOrderByMacauhoiDesc();

    @Query(value = "SELECT * FROM cauhoi WHERE mamonhoc = ?1 AND trangthai = 0 ORDER BY RAND() LIMIT ?2", nativeQuery = true)
    List<Question> findRandomByMonHoc(String maMonHoc, int soCau);
}