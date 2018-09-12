package com.agileengine.crawling.strategies;

import com.agileengine.crawling.CrawlingStrategy;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.stream.Collectors;

public class TitleCrawlingStrategy implements CrawlingStrategy {

    public static final String KEY = "title";

    @Override
    public Elements findElements(Document document, Element originElement) {
        String val = originElement.attr(KEY);
        return document.getElementsByAttributeValue(KEY, val);
    }

    @Override
    public Elements filterElements(Elements elements, Element originElement) {
        return elements.stream()
                .filter(e -> e.attr(KEY).equals(originElement.attr(KEY)))
                .collect(Collectors.toCollection(Elements::new));
    }
}
