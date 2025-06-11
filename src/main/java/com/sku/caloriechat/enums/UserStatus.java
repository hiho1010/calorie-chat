package com.sku.caloriechat.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 계정 상태")
public enum UserStatus {

    @Schema(description = "정상 활동 중")
    ACTIVE,

    @Schema(description = "탈퇴된 사용자")
    DELETED,

    @Schema(description = "비활성 상태 (로그인 불가)")
    INACTIVE,

    @Schema(description = "정지된 사용자")
    SUSPENDED
}