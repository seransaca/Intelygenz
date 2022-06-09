package com.seransaca.intelygenz.securitish.web.dto;

import com.seransaca.intelygenz.securitish.utils.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.seransaca.intelygenz.securitish.web.dto.ValidationMessageErrorConstants.*;
@Data
@Schema(name = "SafeBoxRequest",description = "Request to create a safebox")
public class SafeBoxRequestDTO {

    @NotEmpty(message = ERROR_SAFEBOX_NAME)
    @Size(max = 30, message = ERROR_SAFEBOX_NAME_SIZE)
    private String name;

    @ValidPassword
    @NotEmpty(message = ERROR_SAFEBOX_PASSWORD)
    @Size(max = 32, message = ERROR_SAFEBOX_PASSWORD_SIZE)
    private String password;
}
