package com.nhom6.server.Repository;

import com.nhom6.server.Model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ResultRepository extends JpaRepository<Result, String> {
    Optional<Result> findByKiThi_MakithiAndNguoiDung_Id(String maKiThi, String id);

    List<Result> findByKiThi_Makithi(String maKiThi);

    String findTopByOrderByMaketquaDesc();
}
