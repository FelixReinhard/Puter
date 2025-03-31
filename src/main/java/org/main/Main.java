package org.main;

import org.assembler.Assembler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.utils.FileUtils.readFile;

public class Main {
    public static void main(String[] args) {
        Assembler.assembleIntoCPU(readFile("fib.asm")).run();
    }

}