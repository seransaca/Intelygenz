package com.seransaca.intelygenz.securitish.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(name = "Token")
public class TokenDTO {

    @Schema(description = "Token available for 3 minutes", example = "RkbfZSW5MmcK3b7kcgg")
    private String token;

}
