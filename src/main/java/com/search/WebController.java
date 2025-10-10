package com.search;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class WebController {
    
    private final SearchEngine searchEngine = new SearchEngine();
    
    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam("q") String query) {
        List<Document> results = searchEngine.search(query);
        Map<String, Object> response = new HashMap<>();
        response.put("query", query);
        response.put("results", results);
        response.put("count", results.size());
        return response;
    }
    
    @GetMapping("/documents")
    public Map<String, Object> getAllDocuments() {
        List<Document> docs = searchEngine.getAllDocuments();
        Map<String, Object> response = new HashMap<>();
        response.put("documents", docs);
        response.put("total", docs.size());
        return response;
    }
}
