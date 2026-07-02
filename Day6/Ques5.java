import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

interface WebhookIntegration {
    void sendMessage();
}

@Component
class SlackIntegration implements WebhookIntegration {

    public void sendMessage() {
        System.out.println("Slack Message");
    }
}

@Component
class DiscordIntegration implements WebhookIntegration {

    public void sendMessage() {
        System.out.println("Discord Message");
    }
}

@Component
class EmailIntegration implements WebhookIntegration {

    public void sendMessage() {
        System.out.println("Email Message");
    }
}

@Component
class WebhookDispatcher implements ApplicationContextAware, SmartInitializingSingleton {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Override
    public void afterSingletonsInstantiated() {

        Map<String, WebhookIntegration> map =
                context.getBeansOfType(WebhookIntegration.class);

        System.out.println("Routing Table Ready");
        System.out.println("Total Integrations : " + map.size());
    }
}