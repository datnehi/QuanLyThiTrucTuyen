package com.nhom6.server.Services;

import java.util.List;

public interface SubjectService {
    List<Subject> getAllSubjects();
    List<Subject> getActiveSubjects(); // Lấy các môn học có trạng thái = 0
    Subject createSubject(Subject subject);
    Subject updateSubject(String mamonhoc, Subject subject);
    void deleteSubject(String mamonhoc); // Thay vì xóa, sẽ cập nhật trạng thái = 1
    void toggleSubjectStatus(String mamonhoc);
    List<Subject> searchSubjects(String keyword);
}