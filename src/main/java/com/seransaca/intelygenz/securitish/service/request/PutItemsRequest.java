package com.seransaca.intelygenz.securitish.service.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PutItemsRequest {

    @NotNull(message = Constants.MSG_UUID_NOT_EMPTY)
    private String uuid;

    @NotBlank(message = Constants.MSG_UUID_NOT_EMPTY)
    private List<String> items;
}
