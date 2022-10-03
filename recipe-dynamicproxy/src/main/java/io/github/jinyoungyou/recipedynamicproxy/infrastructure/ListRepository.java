package io.github.jinyoungyou.recipedynamicproxy.infrastructure;

import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;

@Component
public class ListRepository implements CollectionRepository {

    private final CopyOnWriteArrayList<String> l = new CopyOnWriteArrayList<>();

    @Override
    public String add(String e) {
        l.add(e);
        return l.toString();
    }

    @Override
    public String remove(String e) {
        l.remove(e);
        return l.toString();
    }
}
