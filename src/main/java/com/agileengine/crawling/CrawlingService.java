package com.agileengine.crawling;

import com.agileengine.crawling.strategies.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CrawlingService {
    private static Logger LOGGER = LoggerFactory.getLogger(CrawlingService.class);

    private static final List<CrawlingStrategy> strategies = new ArrayList<CrawlingStrategy>() {{
        add(new IdCrawlingStrategy());
        add(new TitleCrawlingStrategy());
        add(new ClassCrawlingStrategy());
        add(new TextCrawlingStrategy());
        add(new HrefCrawlingStrategy());
        add(new RelCrawlingStrategy());
        add(new OnclickCrawlingStrategy());
    }};

    public Optional<Element> findElementById(Document doc, String targetElementId) {
        return Optional.ofNullable(doc.getElementById(targetElementId));
    }

    public Optional<Element> findSimilarElementInDocument(Document diffDocument, Element originElement) {
        for (CrawlingStrategy crawler : strategies) {
            Elements elements = crawler.findElements(diffDocument, originElement);
            if (elements.size() == 1) {
                return Optional.of(elements.get(0));
            }
            if (elements.size() > 1) {
                Optional<Element> uniqueElement = filterElementsRecursively(elements, originElement, 0);
                if (uniqueElement.isPresent()) {
                    return uniqueElement;
                }
            }
        }
        LOGGER.info("No unique element was found in diff document for origin element [id = {}]", originElement.id());
        return Optional.empty();
    }

    //only two filters; complexity = 2
    private Optional<Element> filterElements(Elements elements, Element originElement) {
        for (CrawlingStrategy crawler : strategies) {
            Elements afterFiltering = crawler.filterElements(elements, originElement);
            if (afterFiltering.size() == 1) {
                return Optional.of(afterFiltering.get(0));
            }
        }
        return Optional.empty();
    }

    //all filters;
    /**
     * PLEASE READ COMMENTS BELOW
     * not tested properly. Might have overhead. If you want to test it, please provide test input data or extend time for this task
     * @return
     */
    private Optional<Element> filterElementsRecursively(Elements elements, Element originElement, int startIndex) {
        for (int i = startIndex+1; i < strategies.size(); i++) {
            CrawlingStrategy crawler = strategies.get(i);
            Elements afterFiltering = crawler.filterElements(elements, originElement);
            if (afterFiltering.size() == 1) {
                return Optional.of(afterFiltering.get(0));
            }
            if (afterFiltering.size() == 0) {
                continue;
            }
            return filterElementsRecursively(elements, originElement, ++startIndex);
        }
        return Optional.empty();
    }
}



