package io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RestController;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@RestController
public @interface ProxyController {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    String repositoryName();
}
