package com.nhom6.server.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {
    List<Subject> findByTenmonhocContainingIgnoreCase(String keyword);
    Subject findTopByOrderByMamonhocDesc();
    List<Subject> findByTrangthai(Boolean trangthai);
}