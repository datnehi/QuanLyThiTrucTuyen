package com.nhom6.server.Repository;

import com.nhom6.server.Model.Course;
import com.nhom6.server.Model.PhanMon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhanMonRepository extends JpaRepository<PhanMon, Long> {

    @Query(value = """
        SELECT mh.* FROM phanmon pm 
        JOIN monhoc mh ON pm.mamonhoc = mh.mamonhoc 
        WHERE pm.id = :id AND pm.trangthai = 0
        """, nativeQuery = true)
    List<Course> findCoursesBySinhVienId(@Param("id") String id);
    boolean existsByIdAndMaMonHoc(String id, String mamonhoc);
}
