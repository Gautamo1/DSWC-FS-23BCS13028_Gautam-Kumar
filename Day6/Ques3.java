import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

interface PaymentProcessor {
    void processPayment();
}

@Component
@Primary
class StripeProcessor implements PaymentProcessor {

    public void processPayment() {
        System.out.println("Stripe Payment");
    }
}

@Component
class SquareProcessor implements PaymentProcessor {

    public void processPayment() {
        System.out.println("Square Payment");
    }
}

class BankXmlProcessor implements PaymentProcessor {

    public void processPayment() {
        System.out.println("Bank XML Payment");
    }
}

@Component("bankProcessor")
class BankFactory implements FactoryBean<BankXmlProcessor> {

    public BankXmlProcessor getObject() {
        return new BankXmlProcessor();
    }

    public Class<?> getObjectType() {
        return BankXmlProcessor.class;
    }
}

@Component
class CheckoutService {

    private PaymentProcessor defaultProcessor;
    private PaymentProcessor bankProcessor;

    CheckoutService(PaymentProcessor defaultProcessor,
                    @Qualifier("bankProcessor") PaymentProcessor bankProcessor) {

        this.defaultProcessor = defaultProcessor;
        this.bankProcessor = bankProcessor;
    }

    public void checkout() {

        defaultProcessor.processPayment();
        bankProcessor.processPayment();
    }
}