package com.nhom6.server.Repository;

import com.nhom6.server.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query(value = "SELECT TOP (:soCau) * FROM cauhoi WHERE maMonHoc = :maMonHoc AND trangThai = 0 ORDER BY NEWID()", nativeQuery = true)
    List<Question> findRandomQuestions(@Param("maMonHoc") String maMonHoc, @Param("soCau") int soCau);

    List<Question> findByMaCauHoiIn(List<String> maCauHoiList);

    Optional<Question> findByMaCauHoi(String maCauHoi);
}