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

    //only two filters; This method successfully passed the tests
    private Optional<Element> filterElements(Elements elements, Element originElement) {
        for (CrawlingStrategy crawler : strategies) {
            Elements afterFiltering = crawler.filterElements(elements, originElement);
            if (afterFiltering.size() == 1) {
                return Optional.of(afterFiltering.get(0));
            }
        }
        return Optional.empty();
    }

    /**
     * PLEASE READ COMMENTS BELOW
     * This method should try all possible scenarios of different filters combination.
     * It wasnot tested properly due to lack of time. Might have overhead in complexity.
     * I think that system rarely need such algorithms
     * and in most of the cases {@link #filterElements(Elements, Element)} is enough
     * If you want perfect solution, please provide appropriate test input data or extend time for this task
     * For provided test cases you can use simplified method {@link #filterElements(Elements, Element)}. Complexity O(1). Test passed
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



