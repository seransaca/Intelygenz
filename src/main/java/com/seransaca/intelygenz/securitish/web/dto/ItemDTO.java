package com.seransaca.intelygenz.securitish.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(name = "Item")
public class ItemDTO {

    @Schema(description = "Id of item", example = "1")
    private int itemId;

    @Schema(description = "Name of item", example = "Object 1")
    private String itemName;

}
