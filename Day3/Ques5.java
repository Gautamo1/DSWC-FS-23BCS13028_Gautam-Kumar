import java.util.*;

class Transaction{

    String status;
    String category;
    double amount;

    Transaction(String s,String c,double a){
        status=s;
        category=c;
        amount=a;
    }
}

class SalesAnalyzer{

    double calculateElectronicsRevenue(List<Transaction> list){

        return list.stream()
                .filter(x->x.status.equals("COMPLETED"))
                .filter(x->x.category.equals("ELECTRONICS"))
                .mapToDouble(x->x.amount)
                .sum();
    }
}

public class Ques5{

    public static void main(String[] args){

        List<Transaction> list=new ArrayList<>();

        list.add(new Transaction("COMPLETED","ELECTRONICS",1000));
        list.add(new Transaction("PENDING","ELECTRONICS",500));
        list.add(new Transaction("COMPLETED","BOOKS",300));
        list.add(new Transaction("COMPLETED","ELECTRONICS",700));

        SalesAnalyzer s=new SalesAnalyzer();

        System.out.println(s.calculateElectronicsRevenue(list));
    }
}