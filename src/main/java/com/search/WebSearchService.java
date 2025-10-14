package com.search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebSearchService {
    
    private static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";
    
    public List<SearchResult> search(String query) throws IOException {
        List<SearchResult> results = new ArrayList<>();
        
        String searchUrl = GOOGLE_SEARCH_URL + "?q=" + query.replace(" ", "+") + "&num=10";
        
        try {
            Document doc = Jsoup.connect(searchUrl)
                    .userAgent(USER_AGENT)
                    .timeout(5000)
                    .get();
            
            Elements searchResults = doc.select("div.g");
            
            for (Element result : searchResults) {
                try {
                    Element titleElement = result.selectFirst("h3");
                    String title = titleElement != null ? titleElement.text() : "No title";
                    
                    Element linkElement = result.selectFirst("a[href]");
                    String link = linkElement != null ? linkElement.attr("href") : "#";
                    
                    Element snippetElement = result.selectFirst("div.VwiC3b");
                    String snippet = snippetElement != null ? snippetElement.text() : "No description available";
                    
                    if (!title.equals("No title") && !link.equals("#")) {
                        results.add(new SearchResult(title, link, snippet));
                    }
                    
                    if (results.size() >= 10) break;
                    
                } catch (Exception e) {
                    continue;
                }
            }
            
        } catch (IOException e) {
            throw new IOException("Failed to fetch search results: " + e.getMessage());
        }
        
        return results;
    }
}

