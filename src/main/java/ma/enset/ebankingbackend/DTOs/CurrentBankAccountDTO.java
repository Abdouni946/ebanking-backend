package ma.enset.ebankingbackend.DTOs;

import lombok.Data;
import ma.enset.ebankingbackend.enums.AccountStatus;

import java.util.Date;

@Data
public class CurrentBankAccountDTO {

    private String id;
    private double balance;
    private Date creationDate;
    private CustomerDTO customerDTO;
    private AccountStatus status;
    private double overDraft;

}