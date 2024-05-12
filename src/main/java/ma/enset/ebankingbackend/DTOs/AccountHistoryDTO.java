package ma.enset.ebankingbackend.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String AccountId;
    private Double balance;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private List<AccountOperationDTO> operationsDTO;


}
