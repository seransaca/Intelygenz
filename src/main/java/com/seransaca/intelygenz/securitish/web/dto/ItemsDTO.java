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
@Schema(name = "Items",description = "List of item inside of safebox")
public class ItemsDTO {

    @Schema(description = "List of items", example = "f626c808-648c-41fe-865d-c6062f3e0899")
    private List<ItemDTO> itemList;

}
