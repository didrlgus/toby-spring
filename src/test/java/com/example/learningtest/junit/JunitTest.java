package com.example.learningtest.junit;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class JunitTest {

    static Set<JunitTest> testObjects = new HashSet<>();

    @Test
    public void test1() {

        assertThat(testObjects).doesNotContain(this);

        testObjects.add(this);
    }

    @Test
    public void test2() {

        assertThat(testObjects).doesNotContain(this);

        testObjects.add(this);
    }

    @Test
    public void test3() {

        assertThat(testObjects).doesNotContain(this);

        testObjects.add(this);
    }

}
