package com.ecsimsw.account.service;

import com.ecsimsw.account.domain.User;
import com.ecsimsw.account.domain.UserRepository;
import com.ecsimsw.account.domain.UserRole;
import com.ecsimsw.account.domain.UserRoleRepository;
import com.ecsimsw.account.dto.UpdateUserRoleRequest;
import com.ecsimsw.account.dto.UserInfoAdminResponse;
import com.ecsimsw.account.exception.UserException;
import com.ecsimsw.common.domain.EmailType;
import com.ecsimsw.common.domain.UserStatus;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional(readOnly = true)
    public Page<UserInfoAdminResponse> users(UserStatus status, boolean deleted, Pageable pageable) {
        var users = userRepository.findAllByStatusAndDeleted(status, deleted, pageable);
        return users.map(UserInfoAdminResponse::of);
    }

    @Transactional
    public void approve(Long userId) {
        var user = getUserById(userId);
        user.approve();
        if (user.isTempPassword()) {
            emailService.outbox(
                user.getEmail(),
                EmailType.TEMP_PASSWORD,
                user.getTempPassword()
            );
        }
    }

    @Transactional
    public void disapprove(Long userId) {
        var user = getUserById(userId);
        user.disapprove();
    }

    @Transactional
    public void updateUserRole(Long userId, List<UpdateUserRoleRequest> request) {
        var user = getUserById(userId);
        var roles = request.stream()
            .map(it -> new UserRole(user.getId(), it.type()))
            .toList();
        userRoleRepository.saveAll(roles);
    }

    @Transactional
    public void deleteUser(Long userId) {
        var user = getUserById(userId);
        user.deleted();
        userRepository.save(user);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
    }
}
