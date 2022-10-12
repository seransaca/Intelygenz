package com.seransaca.intelygenz.securitish.web.controller;

import com.seransaca.intelygenz.securitish.web.dto.SafeBoxDTO;
import com.seransaca.intelygenz.securitish.web.dto.SafeBoxRequestDTO;
import com.seransaca.intelygenz.securitish.web.dto.error.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/safebox")
@Tag(name = "SafeBox")
public interface SafeBoxController extends ApiController{

    @Operation(
            summary = "Creating a safebox",
            description = "Create a safebox for a user")
    @ApiResponse(responseCode = "200", description = "The unique id of the safebox")
    @ApiResponse(responseCode = "409", description = "Safebox already exists",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @ApiResponse(responseCode = "422", description = "Malformed expected data",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @ApiResponse(responseCode = "500", description = "Unexpected API error",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SafeBoxDTO> createSafeBox(
            @Valid @RequestBody SafeBoxRequestDTO request
    );
}
