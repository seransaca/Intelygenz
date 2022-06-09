package com.seransaca.intelygenz.securitish.web.controller;

import com.seransaca.intelygenz.securitish.web.dto.ItemsDTO;
import com.seransaca.intelygenz.securitish.web.dto.ItemsRequestDTO;
import com.seransaca.intelygenz.securitish.web.dto.TokenDTO;
import com.seransaca.intelygenz.securitish.web.dto.error.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/safebox/{id}/open")
@Tag(name = "Open")
public interface OpenController extends ApiController{

    @Operation(
            summary = "Open a safebox",
            description = "Open ")
    @ApiResponse(responseCode = "200", description = "Safebox correctly opened")
    @ApiResponse(responseCode = "404", description = "Requested safebox does not exist",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @ApiResponse(responseCode = "422", description = "Malformed expected data",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @ApiResponse(responseCode = "423", description = "Requested safebox is locked",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @ApiResponse(responseCode = "500", description = "Unexpected API error",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TokenDTO> openSafebox(
            @PathVariable("id") String safeboxId
    ) throws Exception;

}
