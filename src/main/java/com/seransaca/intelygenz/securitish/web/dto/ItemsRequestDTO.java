package com.seransaca.intelygenz.securitish.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

import static com.seransaca.intelygenz.securitish.web.dto.ValidationMessageErrorConstants.ERROR_ITEMS;
import static com.seransaca.intelygenz.securitish.web.dto.ValidationMessageErrorConstants.ERROR_SAFEBOX_NAME;

@Data
@Schema(name = "ItemsRequest",description = "Request to put intems into safebox")
public class ItemsRequestDTO {

    @NotEmpty(message = ERROR_ITEMS)
    private List<String> items;

}
