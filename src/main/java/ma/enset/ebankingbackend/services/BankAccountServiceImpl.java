package ma.enset.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankingbackend.DTOs.CustomerDTO;
import ma.enset.ebankingbackend.entities.*;
import ma.enset.ebankingbackend.enums.OperationType;
import ma.enset.ebankingbackend.exeptions.BalanceNotSufficientException;
import ma.enset.ebankingbackend.exeptions.BankAccountNotFoundException;
import ma.enset.ebankingbackend.exeptions.CustomerNotFoundException;
import ma.enset.ebankingbackend.mappers.BankAccountMapperImpl;
import ma.enset.ebankingbackend.repositories.AccountOperationRepository;
import ma.enset.ebankingbackend.repositories.BankAccountReposiroty;
import ma.enset.ebankingbackend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountReposiroty bankAccountReposiroty;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl mapper;

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving  new customer");
        return customerRepository.save(customer);
    }

    @Override
    public SavingAccount saveSavingAccount(double Balance, double rate, Long CustomerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(CustomerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer Not Found");
        SavingAccount savingAccount = new SavingAccount() ;
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreationDate(new Date());
        savingAccount.setBalance(Balance);
        savingAccount.setRate(rate);
        savingAccount.setCustomer(customer);
        return bankAccountReposiroty.save(savingAccount);
    }

    @Override
    public CurrentAccount saveCurrentAccount(double Balance, double OverDraft, Long CustomerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(CustomerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer Not Found");
        CurrentAccount currentAccount = new CurrentAccount() ;

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreationDate(new Date());
        currentAccount.setBalance(Balance);
        currentAccount.setOverDraft(OverDraft);
        currentAccount.setCustomer(customer);
        return bankAccountReposiroty.save(currentAccount);
    }


    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream().map( customer -> mapper.fromCustomer(customer)).collect(Collectors.toList());

        /* List<CustomerDTO> customerDTOS = new ArrayList<>();

        for(Customer customer1:customers){
            CustomerDTO customerDTO = mapper.fromCustomer(customer1);
            customerDTOS.add(customerDTO);
        }

        */
        return customerDTOS;



    }

    @Override
    public BankAccount getBankAccount(String AccountId) throws BankAccountNotFoundException {
            BankAccount bankAccount = bankAccountReposiroty.findById(AccountId)
                    .orElseThrow(() -> new BankAccountNotFoundException("Account Not Found"));
        return bankAccount;
    }

    @Override
    public void debit(String AccountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException{
      BankAccount bankAccount = getBankAccount(AccountId);
       if(bankAccount.getBalance() < amount)
           throw new BalanceNotSufficientException("Insufficient Balance");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setAmount(amount);
        accountOperation.setDateOperation(new Date());
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        accountOperationRepository.save(accountOperation);
    }

    @Override
    public void credit(String AccountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = getBankAccount(AccountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setAmount(amount);
        accountOperation.setDateOperation(new Date());
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        accountOperationRepository.save(accountOperation);

    }

    @Override
    public void transfer(String AccountId, double amount, String AccountDestination) {
            debit(AccountId, amount, "Transfer to "+AccountDestination);
            credit(AccountDestination, amount, "Transfer from "+AccountId);
    }

    @Override
    public List<BankAccount> listBankAccount() {
        return bankAccountReposiroty.findAll();
    }

}
