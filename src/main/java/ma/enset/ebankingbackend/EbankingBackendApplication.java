package ma.enset.ebankingbackend;

import ma.enset.ebankingbackend.entities.AccountOperation;
import ma.enset.ebankingbackend.entities.CurrentAccount;
import ma.enset.ebankingbackend.entities.Customer;
import ma.enset.ebankingbackend.entities.SavingAccount;
import ma.enset.ebankingbackend.entities.BankAccount;
import ma.enset.ebankingbackend.enums.AccountStatus;
import ma.enset.ebankingbackend.enums.OperationType;
import ma.enset.ebankingbackend.exeptions.BankAccountNotFoundException;
import ma.enset.ebankingbackend.exeptions.CustomerNotFoundException;
import ma.enset.ebankingbackend.repositories.AccountOperationRepository;
import ma.enset.ebankingbackend.repositories.BankAccountReposiroty;
import ma.enset.ebankingbackend.repositories.CustomerRepository;
import ma.enset.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

@Bean
    CommandLineRunner start(BankAccountService bankAccountService) {
        return args -> {

          Stream.of("hassan","imane","mohammed").forEach(name -> {
              Customer customer = new Customer();
              customer.setName(name);
              customer.setEmail(name+"@gmail.com");
              bankAccountService.saveCustomer(customer);
          });
            bankAccountService.listCustomer().forEach(customer -> {
                try {
                    bankAccountService.saveSavingAccount(Math.random()*9000,3.5,customer.getId());
                    bankAccountService.saveCurrentAccount(Math.random()*9000,9000,customer.getId());
                    List<BankAccount>  bankAccountList = bankAccountService.listBankAccount();
                      for(BankAccount bankAccount : bankAccountList) {
                            try{
                                bankAccountService.credit(bankAccount.getId(), Math.random() * 9000, "Credit");
                                bankAccountService.debit(bankAccount.getId(), Math.random() * 9000, "Debit");
                            } catch (BankAccountNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                } catch (Exception | CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
        };
    }

   // @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountReposiroty bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("hassan","amira","moutawakil").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach( customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*9000);
                currentAccount.setCreationDate(new Date());
                currentAccount.setStatus(AccountStatus.ACTIVE);
                currentAccount.setOverDraft(9000);
                currentAccount.setCustomer(customer);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*9000);
                savingAccount.setCreationDate(new Date());
                savingAccount.setStatus(AccountStatus.ACTIVE);
                savingAccount.setRate(3.5);
                savingAccount.setCustomer(customer);
                bankAccountRepository.save(savingAccount);

            });

            bankAccountRepository.findAll().forEach(bankAccount ->
            {
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation = new AccountOperation();;
                    accountOperation.setAmount(Math.random()*9000);
                    accountOperation.setDateOperation(new Date());
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setBankAccount(bankAccount);
                    accountOperationRepository.save(accountOperation);
                }
            });



        };
    }
}
