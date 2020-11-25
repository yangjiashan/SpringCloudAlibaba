import java.util.ArrayList;
import java.util.List;

public interface FInterfaceTest<T> {

    public T next();
}


class interfaceText<T> implements FInterfaceTest<T> {
    @Override
    public T next() {
        return null;
    }

    public void test(List<?> f) {
        List<? extends T> nn = new ArrayList<>();

    }

    public <E> void getCount(Class<E>  eClass){


    }
}