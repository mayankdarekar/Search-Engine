package com.search;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebSearchService {
    
    public List<SearchResult> search(String query) {
        List<SearchResult> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        if (lowerQuery.contains("java")) {
            results.add(new SearchResult(
                "Java Tutorial - W3Schools",
                "https://www.w3schools.com/java/",
                "Java is a popular programming language. Java is used to develop mobile apps, web apps, desktop apps, games and much more."
            ));
            results.add(new SearchResult(
                "Java Documentation - Oracle",
                "https://docs.oracle.com/en/java/",
                "Official Java documentation from Oracle. Learn Java programming with tutorials, guides, and API documentation."
            ));
            results.add(new SearchResult(
                "Learn Java Programming - GeeksforGeeks",
                "https://www.geeksforgeeks.org/java/",
                "Java is one of the most popular and widely used programming languages. It is easy to learn and simple to use."
            ));
        } else if (lowerQuery.contains("python")) {
            results.add(new SearchResult(
                "Python Tutorial - W3Schools",
                "https://www.w3schools.com/python/",
                "Python is a popular programming language. Python can be used on a server to create web applications."
            ));
            results.add(new SearchResult(
                "Welcome to Python.org",
                "https://www.python.org/",
                "The official home of the Python Programming Language. Download Python, read documentation, and find community resources."
            ));
            results.add(new SearchResult(
                "Learn Python - Codecademy",
                "https://www.codecademy.com/learn/learn-python-3",
                "Learn Python, a powerful language used by sites like YouTube and Dropbox. Learn the fundamentals of Python syntax."
            ));
        } else if (lowerQuery.contains("web")) {
            results.add(new SearchResult(
                "MDN Web Docs",
                "https://developer.mozilla.org/",
                "Resources for developers, by developers. Learn web development with HTML, CSS, JavaScript tutorials and references."
            ));
            results.add(new SearchResult(
                "Web Development Tutorial - W3Schools",
                "https://www.w3schools.com/",
                "W3Schools is a web developer site, with tutorials and references on web development languages."
            ));
            results.add(new SearchResult(
                "freeCodeCamp - Learn to Code",
                "https://www.freecodecamp.org/",
                "Learn to code for free. Build projects and earn certifications in responsive web design, algorithms, and more."
            ));
        } else if (lowerQuery.contains("algorithm")) {
            results.add(new SearchResult(
                "Introduction to Algorithms - MIT",
                "https://ocw.mit.edu/courses/introduction-to-algorithms/",
                "Learn algorithms from MIT OpenCourseWare. Covers sorting, searching, graph algorithms, and more."
            ));
            results.add(new SearchResult(
                "Algorithms - Khan Academy",
                "https://www.khanacademy.org/computing/computer-science/algorithms",
                "Learn algorithms with free online courses from Khan Academy. Includes binary search, sorting, and graph algorithms."
            ));
            results.add(new SearchResult(
                "Data Structures and Algorithms - GeeksforGeeks",
                "https://www.geeksforgeeks.org/data-structures/",
                "Comprehensive guide to data structures and algorithms with code examples and explanations."
            ));
        } else {
            results.add(new SearchResult(
                query + " - Wikipedia",
                "https://en.wikipedia.org/wiki/" + query.replace(" ", "_"),
                "Learn more about " + query + " on Wikipedia, the free encyclopedia."
            ));
            results.add(new SearchResult(
                "Search " + query + " - Google",
                "https://www.google.com/search?q=" + query.replace(" ", "+"),
                "Find more information about " + query + " on Google Search."
            ));
            results.add(new SearchResult(
                query + " Tutorial",
                "https://www.google.com/search?q=" + query.replace(" ", "+") + "+tutorial",
                "Find tutorials and guides about " + query + " from various online resources."
            ));
        }
        
        return results;
    }
}
