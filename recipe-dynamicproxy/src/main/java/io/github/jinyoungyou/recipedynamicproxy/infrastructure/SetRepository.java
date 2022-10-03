package io.github.jinyoungyou.recipedynamicproxy.infrastructure;

import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.stereotype.Component;

@Component
public class SetRepository implements CollectionRepository {

        private final CopyOnWriteArraySet<String> s = new CopyOnWriteArraySet<>();

        @Override
        public String add(String e) {
            s.add(e);
            return s.toString();
        }

        @Override
        public String remove(String e) {
            s.remove(e);
            return s.toString();
        }
}
