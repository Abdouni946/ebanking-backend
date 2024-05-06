package ma.enset.ebankingbackend.exeptions;

public class CustomerNotFoundException extends Throwable {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
