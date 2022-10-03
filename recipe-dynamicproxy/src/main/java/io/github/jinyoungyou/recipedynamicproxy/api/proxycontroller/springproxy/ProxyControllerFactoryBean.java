package io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller.springproxy;

import io.github.jinyoungyou.recipedynamicproxy.infrastructure.CollectionRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

@Slf4j
@Getter
@Setter
public class ProxyControllerFactoryBean implements FactoryBean<Object>, InitializingBean,
        ApplicationContextAware {

    private ApplicationContext applicationContext;
    private Class<?> clazz;
    private String repositoryName;

    private final ProxyFactoryBean proxyFactoryBean;

    public ProxyControllerFactoryBean() {
        proxyFactoryBean = new ProxyFactoryBean();
    }

    @Override
    public Object getObject() {
        CollectionRepository repository = applicationContext.getBean(repositoryName,
                CollectionRepository.class);
        proxyFactoryBean.setInterfaces(clazz);

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        pointcut.setExpression("(@annotation(org.springframework.web.bind.annotation.GetMapping)"
                + " || @annotation(org.springframework.web.bind.annotation.PostMapping)"
                + " || @annotation(org.springframework.web.bind.annotation.PutMapping)"
                + " || @annotation(org.springframework.web.bind.annotation.DeleteMapping))"
                + " && execution(public java.lang.String *(..))");
        proxyFactoryBean.addAdvisor(
                new DefaultPointcutAdvisor(pointcut, new RepositoryInvocationAdvice(repository)));

        return proxyFactoryBean.getObject();
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
