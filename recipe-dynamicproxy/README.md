# Dynamic Proxy Pattern

## Motivation

Spring Framework 내 널리 사용되는 도구 중 일부는 개발자가 interface만 정의하면 별도의 구현없이 기능을 사용할 수 있도록 제공한다. e.g., repository, feign, mybatis 등
본 모듈에서는 이러한 도구들이 어떤 방식으로 구현되어있는지 살펴보고자 한다.

## Example Problem

## Solution

간단하게 interface로 선언된 두개의 다른 repository를 사용하는 ListController와 SetController를 Dynamic Proxy를 사용해서
구현하겠다.

### 1. Controller 역할을 수행할 Dynamic Proxy Object와 FactoryBean 개발

> io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller.springproxy
> io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller.reflectionproxy

Dynamic Proxy Object 구현을 위해 다음 두가지 방법을 고민했고, 최종적으로 ProxyFactoryBean으로 결정했다.

- Reflection의 Dynamic Proxy + InvocationHandler
- Spring AOP의 ProxyFactoryBean + MethodInterceptor + PointCut

InvocationHandler는 메소드 선정과 Advice가 분리되어 있지 않고 invoke 메서드 내에서 직접 구현해야 한다.
반면에 ProxyFactoryBean을 사용하면, 메소드 선정은 PointCut, Advice는 MethodInterceptor로 각각 분리하여 개발하는 것이 가능하다.
게다가 PointCut 매처와 Advice 역시 여러개를 추가하여 확장 가능한 것도 있고, Spring Framework를 사용 중이니 프레임워크의 기능을 최대한 활용하자라는
생각도 든다.

### 2. ComponentScan을 위한 Annotation 개발

> io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller.annotation

Controller interface에 달아줄 @ProxyController와 컴포넌트 스캔에 사용할 @EnableProxyControllers를 개발했다.
@ProxyController의 인자들은 Dynamic Proxy Object에서 쓰이고,
@EnableProxyControllers의 인자들은 컴포넌트 스캔 과정에서 정보로 활용된다. e.g., basePackages

### 3. ComponentScan 개발

> io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller.annotation

지정된 패지키 내의 @ProxyController가 지정된 interface를 스캔한다. 스캔된 interface들의 annotation poperties를 해석하고, 원하는
형태의 FactoryBean 생성을 위한 BeanDefinition을 정의한다.

## Reference

- [spring-projects/spring-integration](https://github.com/spring-projects/spring-integration)
- [OpenFeign/feign](https://github.com/OpenFeign/feign)
- [mybatis/spring](https://github.com/mybatis/spring)
