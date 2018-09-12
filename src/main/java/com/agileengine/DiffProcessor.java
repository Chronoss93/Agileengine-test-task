package com.agileengine;

import com.agileengine.crawling.CrawlingService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.Optional;
import java.util.function.Function;

public class DiffProcessor {
    private CrawlingService crawlingService = new CrawlingService();
    private DocumentService documentService = new DocumentService();

    public Optional<String> findPathForDiffElement(String originHtmlPath, String diffHtmlPath, String diffElementId) {
        Optional<Document> document = documentService.findDocument(new File(originHtmlPath));
        Optional<Element> element = document.flatMap(findElement(diffElementId));
        Optional<Document> diffDocument = documentService.findDocument(new File(diffHtmlPath));
        Optional<Element> differElement = element.flatMap(findElementInDifferDocument(diffDocument));
        return differElement.flatMap(getPathToElement());
    }

    private Function<Document, Optional<Element>> findElement(String targetElementId) {
        return document -> crawlingService.findElementById(document, targetElementId);
    }

    //2 ways: or put optional as parameter, or additional "if" statement
    private Function<Element, Optional<Element>> findElementInDifferDocument(Optional<Document> diffDocument) {
        return element -> diffDocument.flatMap(document -> crawlingService.findSimilarElementInDocument(document, element));
    }

    private Function<Element, Optional<String>> getPathToElement() {
        return p -> {
            Elements parents = p.parents();
            StringBuilder pathToElement = new StringBuilder(p.tag().getName());
            for (Element element : parents) {
                pathToElement.insert(0, " > ");
                pathToElement.insert(0, element.tag().getName());
            }
            return Optional.of(pathToElement.toString());
        };
    }
}
