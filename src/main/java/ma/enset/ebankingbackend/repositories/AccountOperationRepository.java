package ma.enset.ebankingbackend.repositories;

import ma.enset.ebankingbackend.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

    public List<AccountOperation> findByBankAccountId(String id); // SELECT * FROM AccountOperation WHERE bankAccountId = id
}
