package com.agileengine.crawling.strategies;

import com.agileengine.crawling.CrawlingStrategy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.stream.Collectors;

public class IdCrawlingStrategy implements CrawlingStrategy {
    @Override
    public Elements findElements(Document document, Element originElement) {
        Elements elements = new Elements();
        Element elementById = document.getElementById(originElement.id());
        if (elementById != null) {
            elements.add(elementById);
        }
        return elements;
    }

    @Override
    public Elements filterElements(Elements elements, Element originElement) {
        return elements.stream()
                .filter(e -> e.id().equals(originElement.id()))
                .collect(Collectors.toCollection(Elements::new));
    }
}
