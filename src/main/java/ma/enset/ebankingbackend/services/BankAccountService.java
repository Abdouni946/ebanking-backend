package ma.enset.ebankingbackend.services;

import ma.enset.ebankingbackend.DTOs.CustomerDTO;
import ma.enset.ebankingbackend.entities.CurrentAccount;
import ma.enset.ebankingbackend.entities.Customer;
import ma.enset.ebankingbackend.entities.BankAccount;
import ma.enset.ebankingbackend.entities.SavingAccount;
import ma.enset.ebankingbackend.exeptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    Customer saveCustomer(Customer customer);
    SavingAccount saveSavingAccount(double Balance, double type, Long CustomerId) throws CustomerNotFoundException;
    CurrentAccount saveCurrentAccount(double Balance, double rate, Long CustomerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomer();
    List<BankAccount> listBankAccount();
    BankAccount getBankAccount(String AccountId);
    void debit(String AccountId, double amount, String description);
    void credit(String AccountId, double amount, String description);
    void transfer(String AccountId, double amount, String AccountDestination);


}
