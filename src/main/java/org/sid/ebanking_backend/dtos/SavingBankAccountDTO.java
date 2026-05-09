package org.sid.ebanking_backend.dtos;

import jakarta.persistence.*;
import org.sid.ebanking_backend.enums.AccountStatus;

import java.util.Date;


@Entity

public class SavingBankAccountDTO extends BankAccountDTO {

    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}
