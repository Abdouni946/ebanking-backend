package ma.enset.ebankingbackend.services;

import ma.enset.ebankingbackend.DTOs.*;
import ma.enset.ebankingbackend.entities.CurrentAccount;
import ma.enset.ebankingbackend.entities.BankAccount;
import ma.enset.ebankingbackend.entities.SavingAccount;
import ma.enset.ebankingbackend.exeptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerdto);
    SavingBankAccountDTO saveSavingAccount(double Balance, double type, Long CustomerId) throws CustomerNotFoundException;
    CurrentBankAccountDTO saveCurrentAccount(double Balance, double rate, Long CustomerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomer();
    List<BankAccountDTO> listBankAccount();
    BankAccountDTO getBankAccount(String AccountId);
    void debit(String AccountId, double amount, String description);
    void credit(String AccountId, double amount, String description);
    void transfer(String AccountId, double amount, String AccountDestination);

    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> getHistory(String accountID);

    AccountHistoryDTO getAccountHistory(String id, int page, int size);
}
