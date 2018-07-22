package com.aloievets.searchengine.server.service;

import java.util.Set;

public interface SearchService {

    /**
     * Get the document by key.
     *
     * @param key the key under which the document is stored
     * @return the document by key or null if no document for this key
     */
    String get(String key);

    /**
     * Save the document for the given key.
     *
     * @param key      the key of the document
     * @param document text of the document
     */
    void put(String key, String document);

    /**
     * Find keys of all the documents containing the phrase that is searched.
     *
     * @param phrase space-separated list of words
     * @return set of keys corresponding to search phrase
     */
    Set<String> findDocumentsContainingPhrase(String phrase);
}
