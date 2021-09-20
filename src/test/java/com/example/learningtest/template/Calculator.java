package com.example.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public int calcSum(String filePath) throws IOException {

        return lineReadTemplate(filePath, (line, value) -> value + Integer.parseInt(line), 0);
    }

    public int calcMultiply(String filePath) throws IOException {

        return lineReadTemplate(filePath, (line, value) -> value * Integer.parseInt(line), 1);
    }

    public String concatenate(String filePath) throws IOException {

        return lineReadTemplate(filePath, (line, value) -> value + line, "");
    }

    public int fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));

            return callback.doSomethingWithReader(br);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T initVal) throws IOException {

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            T res = initVal;
            String line = null;

            while((line = br.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }

            return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
