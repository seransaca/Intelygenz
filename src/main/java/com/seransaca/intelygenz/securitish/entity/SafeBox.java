package com.seransaca.intelygenz.securitish.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.seransaca.intelygenz.securitish.entity.EntityValidationConstants.*;

@Data
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "safebox")
public class SafeBox {

    public static final Integer SAFEBOX_RETRIES_INITIANIZED = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = ERROR_SAFEBOX_UUID)
    @Size(max = 36, message = ERROR_SAFEBOX_UUID_SIZE)
    private String uuid;

    @NotEmpty(message = ERROR_SAFEBOX_NAME)
    @Size(max = 30, message = ERROR_SAFEBOX_NAME_SIZE)
    private String name;

    @NotEmpty(message = ERROR_SAFEBOX_PASSWORD)
    @Size(max = 32, message = ERROR_SAFEBOX_PASSWORD_SIZE)
    private String password;

    private Integer blocked;
}
