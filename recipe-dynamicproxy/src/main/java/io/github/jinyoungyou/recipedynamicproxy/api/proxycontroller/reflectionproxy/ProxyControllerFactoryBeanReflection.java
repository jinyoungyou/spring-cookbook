package io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller.reflectionproxy;

import io.github.jinyoungyou.recipedynamicproxy.infrastructure.CollectionRepository;
import java.lang.reflect.Proxy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

@Slf4j
@Getter
@Setter
public class ProxyControllerFactoryBeanReflection implements FactoryBean<Object>, InitializingBean,
        ApplicationContextAware {

    private ApplicationContext applicationContext;
    private Class<?> clazz;
    private String repositoryName;

    @Override
    public Object getObject() {
        CollectionRepository repository = applicationContext.getBean(repositoryName,
                CollectionRepository.class);

        return Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{clazz},
                new ProxyControllerInvocationHandler(repository));
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.hasText(repositoryName, "RepositoryName must be set");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
