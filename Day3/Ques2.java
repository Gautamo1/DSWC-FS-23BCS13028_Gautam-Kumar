import java.util.*;

enum TriageLevel {
    CRITICAL,
    URGENT,
    STABLE
}

class Patient implements Comparable<Patient>{

    String name;
    TriageLevel severity;
    long arrivalTime;

    Patient(String n,TriageLevel s,long t){
        name=n;
        severity=s;
        arrivalTime=t;
    }

    public int compareTo(Patient p){

        if(severity!=p.severity)
            return severity.compareTo(p.severity);

        return Long.compare(arrivalTime,p.arrivalTime);
    }
}

class TriageManager{

    PriorityQueue<Patient> q=new PriorityQueue<>();

    void admitPatient(Patient p){
        q.add(p);
    }

    Patient getNextPatient(){
        return q.poll();
    }
}

public class Ques2{

    public static void main(String[] args){

        TriageManager t=new TriageManager();

        t.admitPatient(new Patient("A",TriageLevel.URGENT,2));
        t.admitPatient(new Patient("B",TriageLevel.CRITICAL,3));
        t.admitPatient(new Patient("C",TriageLevel.CRITICAL,1));

        while(!t.q.isEmpty()){
            System.out.println(t.getNextPatient().name);
        }
    }
}