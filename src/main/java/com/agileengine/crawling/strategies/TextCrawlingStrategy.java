package com.agileengine.crawling.strategies;

import com.agileengine.crawling.CrawlingStrategy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.stream.Collectors;

public class TextCrawlingStrategy implements CrawlingStrategy {
    @Override
    public Elements findElements(Document document, Element originElement) {
        String val = originElement.text();
        return document.getElementsContainingOwnText(val);
    }

    @Override
    public Elements filterElements(Elements elements, Element originElement) {
        return elements.stream()
                .filter(e -> e.text().equals(originElement.text()))
                .collect(Collectors.toCollection(Elements::new));
    }
}
