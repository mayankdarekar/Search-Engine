package com.search;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WikipediaService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public AnswerCard getWikipediaInfo(String query) {
        try {
            // Step 1: Search Wikipedia for the query
            String searchUrl = String.format(
                "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=%s&format=json&utf8=1",
                query.replace(" ", "%20")
            );
            
            String searchResponse = restTemplate.getForObject(searchUrl, String.class);
            JsonNode searchRoot = objectMapper.readTree(searchResponse);
            JsonNode searchResults = searchRoot.path("query").path("search");
            
            // Get the first search result
            if (searchResults.isArray() && searchResults.size() > 0) {
                String pageTitle = searchResults.get(0).path("title").asText();
                
                // Step 2: Get summary for that page
                String summaryUrl = String.format(
                    "https://en.wikipedia.org/api/rest_v1/page/summary/%s",
                    pageTitle.replace(" ", "_")
                );
                
                String summaryResponse = restTemplate.getForObject(summaryUrl, String.class);
                JsonNode summaryRoot = objectMapper.readTree(summaryResponse);
                
                String title = summaryRoot.path("title").asText("");
                String extract = summaryRoot.path("extract").asText("");
                
                if (!extract.isEmpty() && !extract.equals("null")) {
                    // Limit to 400 characters
                    if (extract.length() > 400) {
                        extract = extract.substring(0, 400) + "...";
                    }
                    
                    return new AnswerCard(title, extract, "Wikipedia", null);
                }
            }
            
        } catch (Exception e) {
            System.out.println("Wikipedia search failed: " + e.getMessage());
        }
        
        // Fallback: Always return something
        return new AnswerCard(
            query, 
            "Discover comprehensive information about \"" + query + "\" through the curated search results below. Explore various sources to learn more about this topic.",
            "Search Info",
            null
        );
    }
}
