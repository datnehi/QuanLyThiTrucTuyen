package com.nhom6.server.Repository;

import com.nhom6.server.Model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, String> {
    List<Answer> findByMaCauHoi(String maCauHoi);

    boolean existsByMaCauTraLoiAndLaDapAnTrue(String maCauTraLoi);

    @Modifying
    @Transactional
    @Query("DELETE FROM Answer c WHERE c.maCauHoi = :maCauHoi")
    void deleteByMaCauHoi(@Param("maCauHoi") String maCauHoi);

    Answer findTopByOrderByMaCauTraLoiDesc();
}
