package se.kth.webservice.project.parsing.compare;

/**
 * Created by victoraxelsson on 2017-03-02.
 */
public class ScopedTask<T> implements Runnable {

    private T result;
    private Closure<T> closure;

    public ScopedTask(Closure<T> closure){
        this.closure = closure;
    }

    @Override
    public void run() {
        result = closure.function();
    }

    public T getResult() {
        return result;
    }

}
