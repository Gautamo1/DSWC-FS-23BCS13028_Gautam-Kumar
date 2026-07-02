import java.util.concurrent.*;
import java.util.*;

class Order{

    String id;

    Order(String id){
        this.id=id;
    }
}

class ExchangeManager{

    ConcurrentHashMap<String,CopyOnWriteArrayList<Order>> map=new ConcurrentHashMap<>();

    void placeOrder(String ticker,Order order){

        map.computeIfAbsent(ticker,k->new CopyOnWriteArrayList<>()).add(order);
    }
}

public class Ques3{

    public static void main(String[] args){

        ExchangeManager e=new ExchangeManager();

        e.placeOrder("BTC",new Order("O1"));
        e.placeOrder("BTC",new Order("O2"));

        System.out.println(e.map.get("BTC").size());
    }
}