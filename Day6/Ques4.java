import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

interface PIIProcessor {
}

@Component
class TransactionService implements PIIProcessor {

    public void process() {
        System.out.println("Transaction Done");
    }
}

@Component
class PublicCurrencyService {

    public void showRate() {
        System.out.println("Currency Rate");
    }
}

@Component
class AuditProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {

        if (bean instanceof PIIProcessor) {
            System.out.println("Audit Log : " + beanName);
        }

        return bean;
    }
}