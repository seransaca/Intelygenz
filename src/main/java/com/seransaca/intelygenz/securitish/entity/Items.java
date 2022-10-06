package com.seransaca.intelygenz.securitish.entity;

import lombok.Builder;
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
@Builder
@Table(name = "items")
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = ERROR_SAFEBOX_UUID)
    @Size(max = 36, message = ERROR_SAFEBOX_UUID_SIZE)
    private String uuid;

    @NotEmpty(message = ERROR_ITEM_NAME)
    @Size(max = 50, message = ERROR_ITEM_NAME_SIZE)
    private String item;

}
