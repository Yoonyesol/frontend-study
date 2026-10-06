package com.example.springSecurity.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank String memId,
        @NotBlank String memNm,
        @Size(min = 4) String password) {}