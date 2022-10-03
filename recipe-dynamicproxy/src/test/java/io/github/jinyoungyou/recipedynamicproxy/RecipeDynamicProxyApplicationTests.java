package io.github.jinyoungyou.recipedynamicproxy;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.jinyoungyou.recipedynamicproxy.api.ListController;
import io.github.jinyoungyou.recipedynamicproxy.api.SetController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RecipeDynamicProxyApplicationTests {

    @Autowired
    ListController listController;
    @Autowired
    SetController setController;

    @Test
    void contextLoads() {
        assertThat(listController).isNotNull();
        assertThat(setController).isNotNull();
    }

}
