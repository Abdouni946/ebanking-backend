package ma.enset.ebankingbackend.DTOs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.ebankingbackend.enums.OperationType;

import java.util.Date;


@Data
public class AccountOperationDTO {
    private Long id;
    private Date dateOperation;
    private double amount;
    private String description;
    @Enumerated(EnumType.STRING)
    private OperationType type;

}
