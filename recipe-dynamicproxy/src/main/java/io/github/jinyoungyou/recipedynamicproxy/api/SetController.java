package io.github.jinyoungyou.recipedynamicproxy.api;

import io.github.jinyoungyou.recipedynamicproxy.api.proxycontroller.annotation.ProxyController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@ProxyController(repositoryName = "setRepository")
@RequestMapping("/api/v1")
public interface SetController {

    @GetMapping(path="/addToSet/{element}")
    String add(@PathVariable String element);

    @GetMapping(path="/removeToSet/{element}")
    String remove(@PathVariable String element);
}
