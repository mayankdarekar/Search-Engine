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
            String searchQuery = query.trim();
            String url = String.format(
                "https://en.wikipedia.org/api/rest_v1/page/summary/%s",
                searchQuery.replace(" ", "_")
            );
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            
            String title = root.path("title").asText("");
            String extract = root.path("extract").asText("");
            
            if (extract.isEmpty() || extract.equals("null")) {
                return null;
            }
            
            // Limit to 400 characters
            if (extract.length() > 400) {
                extract = extract.substring(0, 400) + "...";
            }
            
            return new AnswerCard(title, extract, "Wikipedia", null);
            
        } catch (Exception e) {
            System.out.println("Wikipedia API failed for: " + query + " - " + e.getMessage());
            return null;
        }
    }
}
