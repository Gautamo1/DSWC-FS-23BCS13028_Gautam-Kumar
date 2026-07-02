interface PaymentStrategy {
    boolean processPayment(double amount);
}

class CreditCardStrategy implements PaymentStrategy {

    public boolean processPayment(double amount) {
        System.out.println("Credit Card Payment: " + amount);
        return true;
    }
}

class CryptoStrategy implements PaymentStrategy {

    public boolean processPayment(double amount) {
        System.out.println("Crypto Payment: " + amount);
        return true;
    }
}

class TransactionProcessor {

    private PaymentStrategy strategy;

    TransactionProcessor(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    void executeTransaction(double amount) {
        strategy.processPayment(amount);
    }
}

public class Ques4 {

    public static void main(String[] args) {

        TransactionProcessor t = new TransactionProcessor(new CreditCardStrategy());

        t.executeTransaction(1000);

        t.setPaymentStrategy(new CryptoStrategy());

        t.executeTransaction(2500);
    }
}