package com.aloievets.searchengine.server.web;

import com.aloievets.searchengine.server.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/searchengine")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/{key}")
    public String getDocument(@PathVariable String key) {
        return searchService.get(key);
    }

    @PutMapping("/{key}")
    public void putDocument(@PathVariable String key, @RequestBody String document) {
        searchService.put(key, document);
    }

    @GetMapping
    public Set<String> findDocumentsContainingPhrase(@RequestParam String searchPhrase) {
        return searchService.findDocumentsContainingPhrase(searchPhrase);
    }
}
