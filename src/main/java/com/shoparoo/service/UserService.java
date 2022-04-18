package com.shoparoo.service;

import com.shoparoo.dto.AdminUserUpdateDto;
import com.shoparoo.dto.UserDto;
import com.shoparoo.dto.UserUpdateDto;
import com.shoparoo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Page<User> getAllUsersPageable(Integer pageNumber,
                               Integer usersPerPage,
                               String sortField,
                               String sortDir,
                               String gender,
                               String role,
                               Optional<Boolean> enabled,
                               String searchField,
                               String searchKeyword);
    User getUserById(Long id);
    User getUserByEmail(String email);
    User saveNewUser(UserDto userDto);
    void updateUserInfo(String email, UserUpdateDto userUpdateDto, MultipartFile userPhoto);
    void updateUserEmail(String email, UserDto userDto);
    void updateUserPassword(String email, UserDto userDto, String oldPassword);
    void deleteUserById(Long id);
    void sendVerifyEmailToken(Long id);
    void verifyUserEmail(String token);
    void sendResetPasswordMail(String email);
    void checkResetPasswordTokenValidity(String token);
    void resetUserPassword(String token, UserDto userDto);
    void adminUpdateUser(Long id, AdminUserUpdateDto adminUserUpdateDto, MultipartFile userPhoto);
    User addAuthUserToModel(Authentication authentication, Model model);
}
