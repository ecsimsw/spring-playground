package com.ecsimsw.account.service;

import com.ecsimsw.account.domain.User;
import com.ecsimsw.account.domain.UserRepository;
import com.ecsimsw.account.dto.UserInfoAdminResponse;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.error.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserInfoAdminResponse> users(Pageable pageable) {
        var users = userRepository.findAll(pageable);
        return users.map(UserInfoAdminResponse::of);
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
