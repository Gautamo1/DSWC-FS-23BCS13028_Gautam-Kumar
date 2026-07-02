import java.util.*;

class VideoCache<K,V> extends LinkedHashMap<K,V>{

    int size;

    VideoCache(int size){
        super(size,0.75f,true);
        this.size=size;
    }

    protected boolean removeEldestEntry(Map.Entry<K,V> e){
        return super.size()>size;
    }
}

public class Ques4{

    public static void main(String[] args){

        VideoCache<Integer,String> c=new VideoCache<>(3);

        c.put(1,"A");
        c.put(2,"B");
        c.put(3,"C");

        c.get(1);

        c.put(4,"D");

        System.out.println(c);
    }
}