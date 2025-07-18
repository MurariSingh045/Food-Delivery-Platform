package com.ms.AUTH_SERVICE.service;

import com.ms.AUTH_SERVICE.dto.AdminPromotionResponseDto;

public interface AdminService {

    AdminPromotionResponseDto promoteUserToAdmin(String email , String role);

}
