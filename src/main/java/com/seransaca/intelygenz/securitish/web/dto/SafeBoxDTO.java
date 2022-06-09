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
@Schema(name = "Safebox",description = "Box where you can store everything safely")
public class SafeBoxDTO {

    @Schema(description = "Safebox identifier", example = "f626c808-648c-41fe-865d-c6062f3e0899")
    private String id;

}
