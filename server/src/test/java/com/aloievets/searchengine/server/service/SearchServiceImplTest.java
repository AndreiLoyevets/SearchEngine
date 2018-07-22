package com.aloievets.searchengine.server.service;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchServiceImplTest {

    private static final String KEY = "zoo";
    private static final String DOCUMENT = "monkey zebra elephant lion tiger";
    private static final String SEARCH_WORD = "zebra";
    private static final String SEARCH_PHRASE = "zebra elephant";

    private SearchServiceImpl searchService;

    @Before
    public void init() {
        searchService = new SearchServiceImpl();
    }

    @Test
    public void putDocument() {
        searchService.put(KEY, DOCUMENT);

        assertEquals(DOCUMENT, searchService.get(KEY));
    }

    @Test
    public void searchNullPhrase() {
        searchService.put(KEY, DOCUMENT);

        assertTrue(searchService.findDocumentsContainingPhrase(null).isEmpty());
    }

    @Test
    public void searchEmptyPhrase() {
        searchService.put(KEY, DOCUMENT);

        assertTrue(searchService.findDocumentsContainingPhrase("").isEmpty());
    }

    @Test
    public void searchEmptyStorage() {
        assertTrue(searchService.findDocumentsContainingPhrase(SEARCH_PHRASE).isEmpty());
    }

    @Test
    public void searchOneWord() {
        searchService.put(KEY, DOCUMENT);

        assertEquals(Collections.singleton(KEY), searchService.findDocumentsContainingPhrase(SEARCH_WORD));
    }

    @Test
    public void searchOneWordTwoMatchingDocuments() {
        searchService.put(KEY, DOCUMENT);

        String key2 = KEY + "-copy";
        String document2 = DOCUMENT + " rat";
        searchService.put(key2, document2);

        Set<String> resultKeys = searchService.findDocumentsContainingPhrase(SEARCH_WORD);

        assertEquals(2, resultKeys.size());
        assertTrue(resultKeys.contains(KEY));
        assertTrue(resultKeys.contains(key2));
    }

    @Test
    public void searchPhraseTwoDocumentsMatching() {
        searchService.put(KEY, DOCUMENT);

        String key2 = KEY + "-copy";
        String document2 = DOCUMENT + " rat";
        searchService.put(key2, document2);

        Set<String> resultKeys = searchService.findDocumentsContainingPhrase(SEARCH_PHRASE);

        assertEquals(2, resultKeys.size());
        assertTrue(resultKeys.contains(KEY));
        assertTrue(resultKeys.contains(key2));
    }

    @Test
    public void searchPhraseTwoMatchingAndOneReverseOrder() {
        searchService.put(KEY, DOCUMENT);

        String key2 = KEY + "-copy";
        String document2 = DOCUMENT + " rat";
        searchService.put(key2, document2);

        String key3 = "small-zoo";
        String document3 = "elephant zebra lion";
        searchService.put(key3, document3);

        Set<String> resultKeys = searchService.findDocumentsContainingPhrase(SEARCH_PHRASE);

        assertEquals(2, resultKeys.size());
        assertTrue(resultKeys.contains(KEY));
        assertTrue(resultKeys.contains(key2));
    }
}