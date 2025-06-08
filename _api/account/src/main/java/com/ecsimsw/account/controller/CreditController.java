package com.ecsimsw.account.controller;

import com.ecsimsw.account.service.CreditService;
import com.ecsimsw.common.support.annotation.InternalHandler;
import com.ecsimsw.common.dto.ApiResponse;
import com.ecsimsw.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CreditController {

    private final CreditService creditService;

    @InternalHandler
    @PostMapping("/api/account/credit")
    public ApiResponse<Void> add(@RequestParam String username, @RequestParam Long addition) {
        creditService.addCredit(username, addition);
        return ApiResponse.success();
    }

    @InternalHandler
    @PostMapping("/api/account/credit/rollback")
    public ApiResponse<Void> rollbackAddition(@RequestParam String username, @RequestParam Long addition) {
        creditService.rollbackAddition(username, addition);
        return ApiResponse.success();
    }
}
