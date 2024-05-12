package ma.enset.ebankingbackend.web;

import lombok.AllArgsConstructor;
import ma.enset.ebankingbackend.DTOs.AccountHistoryDTO;
import ma.enset.ebankingbackend.DTOs.AccountOperationDTO;
import ma.enset.ebankingbackend.DTOs.BankAccountDTO;
import ma.enset.ebankingbackend.exeptions.BankAccountNotFoundException;
import ma.enset.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class BankAccountRestController {

    private BankAccountService bankAccountService;

    public void BankAccountRestAPI(BankAccountService bankAccountService){
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/bankAccounts/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(id);
    }

    @GetMapping("/bankAccounts")
    public List<BankAccountDTO> listBankAccount(){
        return bankAccountService.listBankAccount();
    }

    @GetMapping("/bankAccounts/{id}/history")
    public List<AccountOperationDTO> getHistory(@PathVariable String id){
        return bankAccountService.getHistory(id);
    }

    @GetMapping("/bankAccounts/{id}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable String id,
                                               @RequestParam(name = "size", defaultValue = "5") int size,
                                               @RequestParam(name = "page", defaultValue = "0") int page){
        return bankAccountService.getAccountHistory(id,page,size);
    }
}
