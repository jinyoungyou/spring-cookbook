package io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller;

import io.github.jinyoungyou.recipedynamicproxy.api.ListController;
import io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller.annotation.EnableProxyControllers;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableProxyControllers(basePackageClasses = {ListController.class})
public class ProxyControllerConfiguration {

}
