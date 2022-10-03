package io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller.springproxy;

import io.github.jinyoungyou.recipedynamicproxy.infrastructure.CollectionRepository;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@RequiredArgsConstructor
public class RepositoryInvocationAdvice implements MethodInterceptor {

    private final CollectionRepository repository;

    @Override
    public Object invoke(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Object[] args = invocation.getArguments();

        return switch (method.getName()) {
            case "add" -> repository.add((String) args[0]);
            case "remove" -> repository.remove((String) args[0]);
            default -> "Error";
        };
    }
}
