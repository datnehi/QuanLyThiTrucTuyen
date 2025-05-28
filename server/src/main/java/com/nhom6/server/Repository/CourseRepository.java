package com.nhom6.server.Repository;

import com.nhom6.server.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    List<Course> findByTrangThaiFalse();

    Optional<Course> findByMaMonHocAndTrangThaiFalse(String maMonHoc);

    boolean existsByMaMonHoc(String maMonHoc);

    List<Course> findByTenMonHocContainingIgnoreCase(String keyword);

    Course findTopByOrderByMaMonHocDesc();

    List<Course> findByTrangThai(Boolean trangthai);
}
