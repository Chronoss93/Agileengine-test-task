package com.agileengine.crawling;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Optional;

public interface CrawlingStrategy {

    Elements findElements(Document document, Element originElement);

    Elements filterElements(Elements elements, Element originElement);
}
