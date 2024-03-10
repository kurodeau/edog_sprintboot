package com.login;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PasswordForm {


    private String password;

    private String confirmPassword;

    @NotEmpty(message = "密碼不能為空")
    @Size(min = 8, message = "密碼長度至少為8位")
    @Pattern(regexp = ".*[A-Z].*", message = "密碼必須包含至少一個大寫字母")
    @Pattern(regexp = ".*[a-z].*", message = "密碼必須包含至少一個小寫字母")
    @Pattern(regexp = ".*\\d.*", message = "密碼必須包含至少一個數字")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
    @NotEmpty(message = "請再次輸入密碼")
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
