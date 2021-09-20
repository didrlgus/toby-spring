package com.example.learningtest.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class CalcSumTest {

    String filePath;
    Calculator calculator;

    @BeforeEach
    void setUp() {
        filePath =  "/Users/kihyun/Desktop/workspace/java/study/toby/src/test/resources/numbers.txt";
        calculator = new Calculator();
    }

    @Test
    void sumOfNumbers() throws IOException {
        int sum = calculator.calcSum(filePath);
        assertThat(sum).isEqualTo(10);
    }

    @Test
    void multiplyOfNumbers() throws IOException {
        int multiply = calculator.calcMultiply(filePath);
        assertThat(multiply).isEqualTo(24);
    }

    @Test
    void concatenate() throws IOException {
        String str = calculator.concatenate(filePath);
        assertThat(str).isEqualTo("1234");
    }

}
