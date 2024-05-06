package ma.enset.ebankingbackend.exeptions;

public class BankAccountNotFoundException extends RuntimeException{

    public BankAccountNotFoundException(String message) {
        super(message);
    }

}
