package com.springfw.springSecurity.member.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @NotBlank String memId,
    @NotBlank String memNm,          // mem_nm이 NOT NULL이므로 이름도 받음
    @Size(min = 4) String password) {}
