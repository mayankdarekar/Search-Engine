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
            String url = String.format(
                "https://en.wikipedia.org/api/rest_v1/page/summary/%s",
                query.replace(" ", "_")
            );
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            
            String title = root.path("title").asText();
            String extract = root.path("extract").asText();
            String imageUrl = root.path("thumbnail").path("source").asText();
            
            if (extract.isEmpty()) {
                return null;
            }
            
            // Limit extract
            if (extract.length() > 350) {
                extract = extract.substring(0, 350) + "...";
            }
            
            // ALWAYS provide an image - use Unsplash random if Wikipedia doesn't have one
            if (imageUrl == null || imageUrl.isEmpty()) {
                imageUrl = String.format("https://source.unsplash.com/400x300/?%s", 
                    query.replace(" ", ","));
            }
            
            return new AnswerCard(title, extract, "Wikipedia", imageUrl);
            
        } catch (Exception e) {
            // If Wikipedia fails, create a basic answer with Unsplash image
            return new AnswerCard(
                query,
                "Information about " + query + " from various sources. Click the links below to learn more.",
                "General Knowledge",
                String.format("https://source.unsplash.com/400x300/?%s", query.replace(" ", ","))
            );
        }
    }
}
