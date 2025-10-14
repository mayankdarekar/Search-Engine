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
        // ALWAYS try Wikipedia FIRST for everything
        AnswerCard wikiAnswer = wikipediaService.getWikipediaInfo(query);
        if (wikiAnswer != null) {
            return wikiAnswer;
        }
        
        // If Wikipedia fails, provide basic info
        return new AnswerCard(
            query,
            "Search results for " + query + ". Click the links below to learn more about this topic.",
            "General",
            null
        );
    }
    
    public List<SearchResult> search(String query) {
        List<SearchResult> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        // Programming topics get special treatment with curated links
        if (lowerQuery.contains("java") && !lowerQuery.contains("javascript")) {
            results.add(new SearchResult("Java Tutorial - W3Schools", "https://www.w3schools.com/java/", "Learn Java programming from scratch with interactive examples."));
            results.add(new SearchResult("Java Documentation - Oracle", "https://docs.oracle.com/en/java/", "Official Java documentation with API references."));
            results.add(new SearchResult("Learn Java - GeeksforGeeks", "https://www.geeksforgeeks.org/java/", "Complete Java tutorials with interview questions."));
            results.add(new SearchResult("Java Programming - Codecademy", "https://www.codecademy.com/learn/learn-java", "Interactive Java course with projects."));
            results.add(new SearchResult("Java Course - Udemy", "https://www.udemy.com/topic/java/", "Top-rated Java courses."));
            results.add(new SearchResult("Java Tutorial - Tutorialspoint", "https://www.tutorialspoint.com/java/", "Free Java tutorial with examples."));
            results.add(new SearchResult("Java Reddit Community", "https://www.reddit.com/r/java/", "Join the Java community."));
            results.add(new SearchResult("Java Stack Overflow", "https://stackoverflow.com/questions/tagged/java", "Find Java programming answers."));
            results.add(new SearchResult("Baeldung - Java", "https://www.baeldung.com/", "Java tutorials for Spring and REST."));
            results.add(new SearchResult("Java YouTube", "https://www.youtube.com/results?search_query=java+tutorial", "Watch Java tutorials."));
        } else if (lowerQuery.contains("python")) {
            results.add(new SearchResult("Python Tutorial - W3Schools", "https://www.w3schools.com/python/", "Learn Python with interactive examples."));
            results.add(new SearchResult("Python.org", "https://www.python.org/", "Official Python documentation."));
            results.add(new SearchResult("Learn Python - Codecademy", "https://www.codecademy.com/learn/learn-python-3", "Interactive Python course."));
            results.add(new SearchResult("Python for Everybody", "https://www.py4e.com/", "Free Python course."));
            results.add(new SearchResult("Real Python", "https://realpython.com/", "High-quality Python tutorials."));
            results.add(new SearchResult("Python Coursera", "https://www.coursera.org/courses?query=python", "University Python courses."));
            results.add(new SearchResult("Python Reddit", "https://www.reddit.com/r/Python/", "Python community."));
            results.add(new SearchResult("Python Stack Overflow", "https://stackoverflow.com/questions/tagged/python", "Python programming help."));
            results.add(new SearchResult("Python GeeksforGeeks", "https://www.geeksforgeeks.org/python-programming-language/", "Complete Python guide."));
            results.add(new SearchResult("Python YouTube", "https://www.youtube.com/results?search_query=python+tutorial", "Watch Python videos."));
        } else if (lowerQuery.contains("javascript") || lowerQuery.contains("js")) {
            results.add(new SearchResult("JavaScript - MDN", "https://developer.mozilla.org/en-US/docs/Web/JavaScript", "Complete JavaScript reference."));
            results.add(new SearchResult("JavaScript.info", "https://javascript.info/", "Modern JavaScript tutorial."));
            results.add(new SearchResult("JavaScript W3Schools", "https://www.w3schools.com/js/", "Interactive JavaScript tutorials."));
            results.add(new SearchResult("freeCodeCamp JavaScript", "https://www.freecodecamp.org/learn/javascript-algorithms-and-data-structures/", "Free JavaScript course."));
            results.add(new SearchResult("JavaScript Tutorial", "https://www.javascripttutorial.net/", "Learn JavaScript step by step."));
            results.add(new SearchResult("Eloquent JavaScript", "https://eloquentjavascript.net/", "Free JavaScript book."));
            results.add(new SearchResult("JavaScript Reddit", "https://www.reddit.com/r/javascript/", "JavaScript community."));
            results.add(new SearchResult("JavaScript Stack Overflow", "https://stackoverflow.com/questions/tagged/javascript", "JavaScript answers."));
            results.add(new SearchResult("JavaScript30", "https://javascript30.com/", "30 JavaScript projects."));
            results.add(new SearchResult("JavaScript YouTube", "https://www.youtube.com/results?search_query=javascript+tutorial", "JavaScript videos."));
        } else {
            // Generic results for EVERYTHING else
            results.add(new SearchResult(query + " - Wikipedia", "https://en.wikipedia.org/wiki/" + query.replace(" ", "_"), "Learn about " + query + " on Wikipedia."));
            results.add(new SearchResult("Search " + query + " - Google", "https://www.google.com/search?q=" + query.replace(" ", "+"), "Find information about " + query + "."));
            results.add(new SearchResult(query + " on YouTube", "https://www.youtube.com/results?search_query=" + query.replace(" ", "+"), "Watch videos about " + query + "."));
            results.add(new SearchResult(query + " Reddit", "https://www.reddit.com/search/?q=" + query.replace(" ", "+"), "Join discussions about " + query + "."));
            results.add(new SearchResult(query + " News", "https://news.google.com/search?q=" + query.replace(" ", "+"), "Latest news about " + query + "."));
            results.add(new SearchResult(query + " Images", "https://www.google.com/search?q=" + query.replace(" ", "+") + "&tbm=isch", "View images of " + query + "."));
            results.add(new SearchResult(query + " - Quora", "https://www.quora.com/search?q=" + query.replace(" ", "+"), "Find answers about " + query + "."));
            results.add(new SearchResult(query + " on Medium", "https://medium.com/search?q=" + query.replace(" ", "+"), "Read articles about " + query + "."));
            results.add(new SearchResult(query + " Tutorial", "https://www.google.com/search?q=" + query.replace(" ", "+") + "+tutorial", "Learn about " + query + "."));
            results.add(new SearchResult(query + " Amazon", "https://www.amazon.com/s?k=" + query.replace(" ", "+"), "Shop for " + query + "."));
        }
        
        return results;
    }
}
