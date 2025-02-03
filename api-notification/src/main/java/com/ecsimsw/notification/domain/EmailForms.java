package com.ecsimsw.notification.domain;

import com.ecsimsw.common.domain.EmailType;
import com.ecsimsw.notification.domain.form.EmailForm;
import com.ecsimsw.notification.domain.form.SignUpForm;
import com.ecsimsw.notification.domain.form.TempPasswordForm;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum EmailForms {

    TempPasswordEmailForm(EmailType.TEMP_PASSWORD, new TempPasswordForm()),
    SignUpEmailForm(EmailType.SIGN_UP, new SignUpForm());

    private final EmailType type;
    private final EmailForm form;

    public static EmailForm formById(EmailType type) {
        return Arrays.stream(EmailForms.values())
            .filter(i -> i.type == type)
            .findAny()
            .map(it -> it.form)
            .orElseThrow(() -> new IllegalArgumentException("Email form not exists"));
    }
}
