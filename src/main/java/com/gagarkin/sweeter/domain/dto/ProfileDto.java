package com.gagarkin.sweeter.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ProfileDto {
    @NotBlank(message = "Email cannot be empty")
    @Email
    private String email;
    @NotBlank(message = "Please enter the password")
    private String password;
    private String passwordNew;
    private String passwordConfirm;
}
