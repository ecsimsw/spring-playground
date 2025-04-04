package com.ecsimsw.account.service;

import com.ecsimsw.account.domain.CreditRepository;
import com.ecsimsw.account.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreditService {

    private final UserRepository userRepository;
    private final CreditRepository creditRepository;

    public void addCredit(String username, Long value) {
        var user = userRepository.findByUsername(username).orElseThrow();
        var credit = creditRepository.findByUidWithLock(user.getId()).orElseThrow();
        credit.add(value);
    }
}
