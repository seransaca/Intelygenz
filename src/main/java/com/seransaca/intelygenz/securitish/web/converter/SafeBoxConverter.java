package com.seransaca.intelygenz.securitish.web.converter;

import com.seransaca.intelygenz.securitish.entity.SafeBox;
import com.seransaca.intelygenz.securitish.service.exceptions.MalformedDataException;
import com.seransaca.intelygenz.securitish.web.dto.SafeBoxDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * This class is in charge of converting the SafeBox entity to DTO.
 *
 * @author Sergio A. Sánchez Camarero
 */

@Mapper
public interface SafeBoxConverter {

    /**
     * Convert a <code>SafeBox</code> entity to its DTO <code>SafeBoxDto</code>
     *
     * @param safebox Una entidad hotel
     * @return El DTO resultante de la conversión.
     */
    @Mapping(target="id", source="uuid")
    SafeBoxDTO safeboxToDto(SafeBox safebox) throws MalformedDataException;
}
