package org.sid.ebanking_backend.dtos;

import lombok.Data;

@Data
public class CustomerDTO {
    private long id;
    private String name;
    private String email;
}
