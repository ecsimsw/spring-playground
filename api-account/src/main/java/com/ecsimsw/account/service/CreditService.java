package com.ecsimsw.account.service;

import com.ecsimsw.account.domain.Credit;
import com.ecsimsw.account.domain.CreditRepository;
import com.ecsimsw.account.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreditService {

    private final UserRepository userRepository;
    private final CreditRepository creditRepository;

    @Transactional
    public void addCredit(String username, Long value) {
        var user = userRepository.findByUsername(username).orElseThrow();
        var credit = creditRepository.findByUidWithLock(user.getId())
            .orElse(new Credit(user.getId(), 0L));
        credit.add(value);
        creditRepository.save(credit);
    }
}
