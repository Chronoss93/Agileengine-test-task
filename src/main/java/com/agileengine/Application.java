package com.agileengine;

import java.util.Optional;


public class Application {

    public static void main(String[] args) {
        String targetElementId = "make-everything-ok-button";
        DiffProcessor diffProcessor = new DiffProcessor();
        Optional<String> path = diffProcessor.findPathForDiffElement(args[0], args[1], targetElementId);
        System.out.println(path.orElse("No element was found"));
    }
}