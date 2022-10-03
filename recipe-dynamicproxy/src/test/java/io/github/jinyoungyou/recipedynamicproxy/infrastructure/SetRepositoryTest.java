package io.github.jinyoungyou.recipedynamicproxy.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SetRepositoryTest {

    SetRepository setRepository;

    @BeforeEach
    void setUp() {
        setRepository = new SetRepository();

        setRepository.add("ASDF");
        setRepository.add("123");
    }

    @Test
    void add() {
        // when
        String result = setRepository.add("TEST");

        // then
        assertThat(result).contains("TEST");
    }

    @Test
    void remove() {
        // when
        String result = setRepository.remove("ASDF");

        // then
        assertThat(result).doesNotContain("ASDF");
    }

}