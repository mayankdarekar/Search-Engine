package com.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebSearchService {
    
    @Autowired
    private WikipediaService wikipediaService;
    
    public AnswerCard getDirectAnswer(String query) {
        String lowerQuery = query.toLowerCase();
        
        // Try common knowledge first
        if (lowerQuery.contains("hello") || lowerQuery.equals("hi")) {
            return new AnswerCard("What is Hello?", 
                "Hello is a greeting or salutation used when meeting someone or answering the phone. It's one of the most common ways to greet people in English-speaking countries.", 
                "Language", null);
        }
        
        if (lowerQuery.contains("photosynthesis")) {
            return new AnswerCard("What is Photosynthesis?", 
                "Photosynthesis is the process by which green plants use sunlight to synthesize nutrients from carbon dioxide and water. It converts light energy into chemical energy stored in glucose and produces oxygen.", 
                "Biology", null);
        }
        
        // Try Wikipedia for everything else
        AnswerCard wikiAnswer = wikipediaService.getWikipediaInfo(query);
        if (wikiAnswer != null) {
            return wikiAnswer;
        }
        
        return null;
    }
    
    public List<SearchResult> search(String query) {
        List<SearchResult> results = new ArrayList<>();
        
        results.add(new SearchResult(query + " - Wikipedia", "https://en.wikipedia.org/wiki/" + query.replace(" ", "_"), "Learn about " + query + " on Wikipedia."));
        results.add(new SearchResult("Search " + query + " - Google", "https://www.google.com/search?q=" + query.replace(" ", "+"), "Find the latest information about " + query + "."));
        results.add(new SearchResult(query + " on YouTube", "https://www.youtube.com/results?search_query=" + query.replace(" ", "+"), "Watch videos about " + query + "."));
        results.add(new SearchResult(query + " Reddit", "https://www.reddit.com/search/?q=" + query.replace(" ", "+"), "Join discussions about " + query + "."));
        results.add(new SearchResult(query + " News", "https://news.google.com/search?q=" + query.replace(" ", "+"), "Read latest news about " + query + "."));
        results.add(new SearchResult(query + " Images", "https://www.google.com/search?q=" + query.replace(" ", "+") + "&tbm=isch", "View images of " + query + "."));
        results.add(new SearchResult(query + " - Quora", "https://www.quora.com/search?q=" + query.replace(" ", "+"), "Find answers about " + query + "."));
        results.add(new SearchResult(query + " on Twitter", "https://twitter.com/search?q=" + query.replace(" ", "+"), "See what people are saying about " + query + "."));
        results.add(new SearchResult(query + " Tutorial", "https://www.google.com/search?q=" + query.replace(" ", "+") + "+tutorial", "Find tutorials about " + query + "."));
        results.add(new SearchResult(query + " on Amazon", "https://www.amazon.com/s?k=" + query.replace(" ", "+"), "Shop for " + query + " on Amazon."));
        
        return results;
    }
}
