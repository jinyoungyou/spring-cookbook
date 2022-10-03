package io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller.annotation;

import io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller.springproxy.ProxyControllerFactoryBean;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

@Slf4j
public class ProxyControllerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware,
        EnvironmentAware {

    // patterned after Spring Cloud OpenFeign FeignClientsRegistrar
    private ResourceLoader resourceLoader;
    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata,
                                        BeanDefinitionRegistry registry) {
        LinkedHashSet<BeanDefinition> candidateComponents = new LinkedHashSet<>();
        Map<String, Object> attrs = metadata.getAnnotationAttributes(
                EnableProxyControllers.class.getName());
        final Class<?>[] proxyControllers =
                attrs != null ? (Class<?>[]) attrs.get("proxyControllers") : null;

        // Component Scan
        if (proxyControllers == null || proxyControllers.length == 0) {
            ClassPathScanningCandidateComponentProvider scanner = getScanner();
            scanner.setResourceLoader(this.resourceLoader);
            scanner.addIncludeFilter(new AnnotationTypeFilter(ProxyController.class));
            Set<String> basePackages = getBasePackages(metadata);
            for (String basePackage : basePackages) {
                candidateComponents.addAll(scanner.findCandidateComponents(basePackage));
            }
        }
        // Manual Setup
        else {
            for (Class<?> clazz : proxyControllers) {
                candidateComponents.add(new AnnotatedGenericBeanDefinition(clazz));
            }
        }

        for (BeanDefinition candidateComponent : candidateComponents) {
            if (candidateComponent instanceof AnnotatedBeanDefinition beanDefinition) {
                AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                Assert.isTrue(annotationMetadata.isInterface(),
                        "@ProxyController can only be specified on an interface");

                Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(
                        ProxyController.class.getCanonicalName());
                Assert.notNull(attributes,
                        "Attribute repositoryName must be specified on an annotation @ProxyController");

                registerProxyController(registry, annotationMetadata, attributes);
            }
        }
    }

    private void registerProxyController(BeanDefinitionRegistry registry,
                                         AnnotationMetadata annotationMetadata,
                                         Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        String repositoryName = (String)attributes.get("repositoryName");

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
                ProxyControllerFactoryBean.class);
        builder.addPropertyValue("clazz", className);
        builder.addPropertyValue("repositoryName", repositoryName);
        builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        builder.setLazyInit(false);

        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, className);
        beanDefinition.setPrimary(true);

        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition,
                className);
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, registry);
    }

    private Set<String> getBasePackages(AnnotationMetadata metadata) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(
                EnableProxyControllers.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class<?>[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(metadata.getClassName()));
        }

        return basePackages;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isIndependent()
                        && !beanDefinition.getMetadata().isAnnotation();
            }
        };
    }
}
