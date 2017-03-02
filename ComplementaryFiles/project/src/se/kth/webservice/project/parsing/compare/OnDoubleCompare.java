package se.kth.webservice.project.parsing.compare;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victoraxelsson on 2017-03-02.
 */
public abstract class OnDoubleCompare<T> implements OnCompare<T>{

    int counter;

    List<T> queue;

    public OnDoubleCompare(){
        counter = 0;
        queue = new ArrayList<T>();
    }

    public abstract void doubleCompare(T a1, T b1, T a2, T b2 );

    @Override
    public void compare(T a, T b) {

        queue.add(a);
        queue.add(b);

        if(counter > 0 && counter % 2 == 0 && queue.size() >= 4){
            doubleCompare(queue.get(0), queue.get(1), queue.get(2), queue.get(3));
            queue = new ArrayList<T>();
        }

        counter++;
    }

    @Override
    public void flush() {
        if(queue.size() > 0 && queue.size() % 2 == 0 ){
            doubleCompare(queue.get(0), queue.get(1),null, null);
        }

        queue = new ArrayList<T>();
    }
}
