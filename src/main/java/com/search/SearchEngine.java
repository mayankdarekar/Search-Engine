package com.search;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class SearchEngine {
    private List<Document> documents;
    
    public SearchEngine() {
        this.documents = new ArrayList<>();
        loadDocuments();
    }
    
    private void loadDocuments() {
        try {
            Path docsPath = Paths.get("src/main/resources/documents");
            
            if (!Files.exists(docsPath)) {
                Files.createDirectories(docsPath);
                createSampleDocuments();
            }
            
            Files.list(docsPath)
                .filter(path -> path.toString().endsWith(".txt"))
                .forEach(path -> {
                    try {
                        String name = path.getFileName().toString().replace(".txt", "");
                        String content = Files.readString(path);
                        documents.add(new Document(name, content));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            
            System.out.println("📚 Loaded " + documents.size() + " documents");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void createSampleDocuments() throws IOException {
        Map<String, String> samples = Map.of(
            "java_basics.txt", 
            "Java is a high-level, object-oriented programming language developed by Sun Microsystems. It's platform-independent and runs on the Java Virtual Machine (JVM). Java is widely used for enterprise applications, Android development, and web applications. Key features include automatic memory management, strong type checking, and extensive libraries.",
            
            "python_intro.txt",
            "Python is a versatile, high-level programming language known for its simple and readable syntax. Created by Guido van Rossum, Python emphasizes code readability and allows developers to express concepts in fewer lines of code. It's popular in data science, machine learning, web development, and automation. Python has a vast ecosystem of libraries and frameworks.",
            
            "data_structures.txt",
            "Data structures are fundamental concepts in computer science that organize and store data efficiently. Common structures include arrays for sequential storage, linked lists for dynamic insertion, stacks for LIFO operations, queues for FIFO operations, trees for hierarchical data, graphs for network relationships, and hash tables for fast lookups. Choosing the right data structure is crucial for algorithm performance.",
            
            "web_development.txt",
            "Web development involves creating websites and web applications. Frontend development uses HTML for structure, CSS for styling, and JavaScript for interactivity. Backend development handles server-side logic using languages like Java, Python, PHP, or Node.js. Modern web development includes responsive design, RESTful APIs, databases, and frameworks like React, Angular, Vue.js, Spring Boot, and Django.",
            
            "algorithms.txt",
            "Algorithms are step-by-step procedures for solving computational problems. Common categories include sorting algorithms (bubble sort, merge sort, quick sort), searching algorithms (linear search, binary search), graph algorithms (Dijkstra's, BFS, DFS), and dynamic programming. Algorithm efficiency is measured using Big O notation, which describes time and space complexity."
        );
        
        Path docsPath = Paths.get("src/main/resources/documents");
        for (Map.Entry<String, String> entry : samples.entrySet()) {
            Files.writeString(docsPath.resolve(entry.getKey()), entry.getValue());
        }
    }
    
    public List<Document> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String[] queryWords = query.toLowerCase().split("\\s+");
        
        for (Document doc : documents) {
            double score = calculateScore(doc, queryWords);
            doc.setScore(score);
        }
        
        return documents.stream()
            .filter(doc -> doc.getScore() > 0)
            .sorted((d1, d2) -> Double.compare(d2.getScore(), d1.getScore()))
            .collect(Collectors.toList());
    }
    
    private double calculateScore(Document doc, String[] queryWords) {
        String content = doc.getContent().toLowerCase();
        double score = 0.0;
        
        for (String word : queryWords) {
            int count = countOccurrences(content, word);
            score += count * 2;
            
            if (doc.getName().toLowerCase().contains(word)) {
                score += 10;
            }
        }
        
        return score;
    }
    
    private int countOccurrences(String text, String word) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }
        return count;
    }
    
    public List<Document> getAllDocuments() {
        return new ArrayList<>(documents);
    }
}
