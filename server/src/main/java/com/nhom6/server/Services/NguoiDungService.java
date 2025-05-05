package com.nhom6.server.Services;

import com.nhom6.server.Model.NguoiDung;
import java.util.List;

public interface NguoiDungService {
    List<NguoiDung> getAllUsers();
    List<NguoiDung> getActiveUsers();
    NguoiDung createUser(NguoiDung nguoiDung);
    NguoiDung updateUser(String id, NguoiDung nguoiDung);
    void deleteUser(String id);
    void toggleUserStatus(String id);
    List<NguoiDung> searchUsers(String keyword);


}