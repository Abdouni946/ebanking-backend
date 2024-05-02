package ma.enset.ebankingbackend.repositories;

import ma.enset.ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountReposiroty extends JpaRepository<BankAccount, String> {

}
