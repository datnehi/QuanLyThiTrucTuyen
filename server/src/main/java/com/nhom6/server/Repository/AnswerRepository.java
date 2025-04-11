package com.nhom6.server.Repository;

import com.nhom6.server.Model.CauTraLoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<CauTraLoi, String> {
    List<CauTraLoi> findByMacauhoi(String macauhoi);

    @Transactional
    @Modifying
    @Query("DELETE FROM CauTraLoi c WHERE c.macauhoi = :macauhoi")
    void deleteByMacauhoi(@Param("macauhoi") String macauhoi);
    CauTraLoi findTopByOrderByMacautraloiDesc();
}
