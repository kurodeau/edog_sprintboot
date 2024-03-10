package com.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.login.PasswordForm;

public class SellerPasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PasswordForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordForm form = (PasswordForm) target;

        // Validate sellerPassword
        if (form.getPassword() == null) {
            errors.rejectValue("password", "sellerPassword", "沒有輸入密碼");
        }
        // Validate passwordAgain
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "passwordAgain", "密碼不一致");
        }

        // Validate password format
        if (!form.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            errors.rejectValue("password", "sellerPassword", "密碼必須包含至少一个大寫字母，一个小寫字母和一個數字，至少8位");
        }
    }
}
