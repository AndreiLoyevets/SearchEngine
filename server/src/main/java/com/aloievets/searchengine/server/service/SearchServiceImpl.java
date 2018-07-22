package com.aloievets.searchengine.server.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.Validate.notNull;

@Service
public class SearchServiceImpl implements SearchService {

    private final Map<String, String> documents = new HashMap<>();
    private final Map<Integer, Set<String>> wordHashToDocumentIds = new ConcurrentHashMap<>();

    @Override
    public String get(String key) {
        return documents.get(key);
    }

    @Override
    public void put(String key, String document) {
        notNull(document);

        documents.put(key, document);
        updateIndex(key, document);
    }

    @Override
    public Set<String> findDocumentsContainingPhrase(String phrase) {
        if (StringUtils.isEmpty(phrase)) {
            return Collections.emptySet();
        }

        List<Integer> wordsHashes = Arrays.stream(extractWords(phrase))
                .map(Object::hashCode)
                .collect(Collectors.toList());

        Set<String> documentIdsToCheck = lookupIndexForHashes(wordsHashes);

        return documentIdsToCheck.stream()
                .filter(documentId -> documents.containsKey(documentId) && documents.get(documentId).contains(phrase))
                .collect(Collectors.toSet());
    }

    private String[] extractWords(String text) {
        return text.split("\\s+");
    }

    /**
     * Split document by words and store the info that a concrete word is used in a concrete document. For search speedup.
     *
     * @param key      the key corresponding to the document
     * @param document the document matching the key
     */
    private void updateIndex(String key, String document) {
        String[] words = document.split("\\s+");

        Arrays.stream(words).map(Object::hashCode).forEach(hash -> {
            wordHashToDocumentIds.putIfAbsent(hash, new HashSet<>()); // ok due to ConcurrentHashMap
            wordHashToDocumentIds.get(hash).add(key);
        });
    }

    /**
     * Limit the scope of all documents to search before more precise search. Lookups for documents containing all
     * words for given hash codes.
     *
     * @param hashes hash codes of words that are searched in documents
     * @return ids of the documents such that each document contains every word from the given hash codes
     */
    private Set<String> lookupIndexForHashes(List<Integer> hashes) {
        Set<String> matchingDocumentIds = new HashSet<>();

        for (int i = 0; i < hashes.size(); i++) {
            int hash = hashes.get(i);
            Set<String> documentIdsContainingHash = wordHashToDocumentIds.getOrDefault(hash, Collections.emptySet());

            if (documentIdsContainingHash.isEmpty()) {
                return Collections.emptySet();
            }

            if (i > 0) {
                matchingDocumentIds.retainAll(documentIdsContainingHash);
            } else {
                matchingDocumentIds = documentIdsContainingHash;
            }
        }

        return matchingDocumentIds;
    }
}
