import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.List;

interface TradingStrategy {
    void executeTrade();
}

abstract class AbstractStrategy implements TradingStrategy {

    protected String assetClass;
}

@Component
class MomentumStrategy extends AbstractStrategy {

    MomentumStrategy() {
        assetClass = "Stocks";
    }

    public void executeTrade() {
        System.out.println("Momentum Trade");
    }
}

@Component
class ArbitrageStrategy extends AbstractStrategy {

    ArbitrageStrategy() {
        assetClass = "Forex";
    }

    public void executeTrade() {
        System.out.println("Arbitrage Trade");
    }
}

@Component
class MarketDataService {

    public void loadData() {
        System.out.println("Market Data Ready");
    }
}

@Component
class AlertService {

    public void sendAlert() {
        System.out.println("Alert Sent");
    }
}

@Component
class TradingEngine implements BeanNameAware, InitializingBean {

    private final MarketDataService marketDataService;
    private final List<TradingStrategy> strategies;

    private AlertService alertService;

    TradingEngine(MarketDataService marketDataService,
                  List<TradingStrategy> strategies) {

        this.marketDataService = marketDataService;
        this.strategies = strategies;
    }

    @Autowired(required = false)
    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Bean Name : " + name);
    }

    @PostConstruct
    public void start() {
        System.out.println("Cache Warmed");
    }

    @Override
    public void afterPropertiesSet() {

        if (marketDataService != null && !strategies.isEmpty()) {
            System.out.println("Validation Done");
        }
    }

    @PreDestroy
    public void stop() {
        System.out.println("Closing Positions");
    }

    public void run() {

        marketDataService.loadData();

        for (TradingStrategy t : strategies) {
            t.executeTrade();
        }

        if (alertService != null) {
            alertService.sendAlert();
        }
    }
}