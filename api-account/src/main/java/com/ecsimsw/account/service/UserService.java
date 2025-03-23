package com.ecsimsw.account.service;

import com.ecsimsw.account.domain.User;
import com.ecsimsw.account.domain.UserRepository;
import com.ecsimsw.account.dto.SignUpRequest;
import com.ecsimsw.account.dto.UserInfoResponse;
import com.ecsimsw.common.error.ErrorType;
import com.ecsimsw.common.error.UserException;
import com.ecsimsw.common.service.AuthClient;
import com.ecsimsw.common.service.dto.AuthUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthClient authClient;

    public Long create(SignUpRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserException(ErrorType.USER_ALREADY_EXISTS);
        }
        var user = userRepository.save(request.toUser());
        var authResponse = authClient.createAuth(request.toAuthCreationRequest(user.getId()));
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            userRepository.deleteById(user.getId());
            throw new IllegalArgumentException("Invalid auth server response");
        }
        return user.getId();
    }

    @Transactional(readOnly = true)
    public UserInfoResponse userInfo(String userName) {
        var user = getByUsername(userName);
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
        var authResponse = authClient.updateAuth(new AuthUpdateRequest(username, password));
        if (authResponse.getStatusCode() != HttpStatus.OK) {
            userRepository.deleteById(user.getId());
            throw new IllegalArgumentException("Invalid auth server response");
        }
    }

    private User getByUsername(String username) {
        return userRepository.findByUsernameAndDeletedFalse(username)
            .orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
    }
}
