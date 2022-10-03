package io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller.reflectionproxy;

import io.github.jinyoungyou.recipedynamicproxy.infrastructure.CollectionRepository;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

@RequiredArgsConstructor
public class ProxyControllerInvocationHandler implements InvocationHandler {

    private final CollectionRepository repository;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Assert.isTrue(String.class == method.getReturnType(), "Return type must be String");

        return switch (method.getName()) {
            case "equals" -> this.equals(args[0]);
            case "hashCode" -> this.hashCode();
            case "toString" -> this.toString();
            case "add" -> repository.add((String) args[0]);
            case "remove" -> repository.remove((String) args[0]);
            default -> null;
        };
    }
}
