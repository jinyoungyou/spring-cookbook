package io.github.jinyoungyou.recipedynamicproxy.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListRepositoryTest {

    ListRepository listRepository;

    @BeforeEach
    void setUp() {
        listRepository = new ListRepository();

        listRepository.add("ASDF");
        listRepository.add("123");
    }

    @Test
    void add() {
        // when
        String result = listRepository.add("TEST");

        // then
        assertThat(result).contains("TEST");
    }

    @Test
    void remove() {
        // when
        String result = listRepository.remove("ASDF");

        // then
        assertThat(result).doesNotContain("ASDF");
    }
}