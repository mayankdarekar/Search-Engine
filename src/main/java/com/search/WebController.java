package com.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class WebController {
    
    @Autowired
    private WebSearchService webSearchService;
    
    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam String q) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<SearchResult> results = webSearchService.search(q);
            response.put("query", q);
            response.put("count", results.size());
            response.put("results", results);
            response.put("success", true);
        } catch (Exception e) {
            response.put("query", q);
            response.put("count", 0);
            response.put("results", new ArrayList<>());
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }
}
