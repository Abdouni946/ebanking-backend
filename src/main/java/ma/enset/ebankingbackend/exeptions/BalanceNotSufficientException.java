package ma.enset.ebankingbackend.exeptions;

public class BalanceNotSufficientException extends RuntimeException{

        public BalanceNotSufficientException(String message) {
            super(message);
        }
}
