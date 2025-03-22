package com.ecsimsw.account.service;

import com.ecsimsw.account.dto.SignUpRequest;
import com.ecsimsw.account.dto.UserInfoResponse;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.error.UserException;
import com.ecsimsw.domain.User;
import com.ecsimsw.domain.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long create(SignUpRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserException(ErrorType.USER_ALREADY_EXISTS);
        }
        var entity = request.toEntity();
        userRepository.save(entity);
        return entity.getId();
    }

    @Transactional(readOnly = true)
    public UserInfoResponse userInfo(String username) {
        var user = getByUsername(username);
        return UserInfoResponse.of(user);
    }

    @Transactional
    public void delete(String username) {
        var user = getByUsername(username);
        user.deleted();
    }

    @Transactional
    public void updatePassword(String username, String password) {
        var user = getByUsername(username);
//        user.changePassword(passwordEncoder, password);
    }

    private User getByUsername(String username) {
        return userRepository.findByUsernameAndDeletedFalse(username)
            .orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
    }
}
