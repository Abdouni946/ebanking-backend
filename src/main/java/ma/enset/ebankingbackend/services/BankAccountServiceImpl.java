package ma.enset.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankingbackend.DTOs.*;
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
    public CustomerDTO saveCustomer(CustomerDTO customerdto) {
        log.info("Saving  new customer");
        Customer customer = mapper.toCustomer(customerdto);
        Customer savedCustomer = customerRepository.save(customer);
        return  mapper.fromCustomer(savedCustomer);
    }

    @Override
    public SavingBankAccountDTO saveSavingAccount(double Balance, double rate, Long CustomerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(CustomerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer Not Found");
        SavingAccount savingAccount = new SavingAccount() ;
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreationDate(new Date());
        savingAccount.setBalance(Balance);
        savingAccount.setRate(rate);
        savingAccount.setCustomer(customer);
        SavingAccount  saveBankAccount = bankAccountReposiroty.save(savingAccount);
        return mapper.fromSavingBankAccount(saveBankAccount);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentAccount(double Balance, double OverDraft, Long CustomerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(CustomerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer Not Found");
        CurrentAccount currentAccount = new CurrentAccount() ;

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreationDate(new Date());
        currentAccount.setBalance(Balance);
        currentAccount.setOverDraft(OverDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount  saveBankAccount = bankAccountReposiroty.save(currentAccount);
    return mapper.fromCurrentBankAccount(saveBankAccount);
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
    public BankAccountDTO getBankAccount(String AccountId) throws BankAccountNotFoundException {
            BankAccount bankAccount = bankAccountReposiroty.findById(AccountId)
                    .orElseThrow(() -> new BankAccountNotFoundException("Account Not Found"));
            if(bankAccount instanceof SavingAccount){
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return mapper.fromSavingBankAccount(savingAccount);}
            else{
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return mapper.fromCurrentBankAccount(currentAccount);
            }
    }

    @Override
    public void debit(String AccountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException{
        BankAccount bankAccount = bankAccountReposiroty.findById(AccountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account Not Found"));
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
        BankAccount bankAccount = bankAccountReposiroty.findById(AccountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account Not Found"));
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
    public List<BankAccountDTO> listBankAccount() {
        List<BankAccount> listBankAccount = bankAccountReposiroty.findAll();
        List<BankAccountDTO> bankAccountDTOS = listBankAccount
                .stream().map(bankAccount -> {
            if(bankAccount instanceof SavingAccount){
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return mapper.fromSavingBankAccount(savingAccount);}
            else{
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return mapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

     @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).
                 orElseThrow(() -> new CustomerNotFoundException("Customer Not Found"));
        return mapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = mapper.toCustomer(customerDTO);
        Customer updatedCustomer = customerRepository.save(customer);
        return mapper.fromCustomer(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);

    }

    @Override
    public List<AccountOperationDTO> getHistory(String accountID){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountID);
        return accountOperations.stream().map(accountOperation -> mapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String id, int page, int size) {
        return null;

    }

}
