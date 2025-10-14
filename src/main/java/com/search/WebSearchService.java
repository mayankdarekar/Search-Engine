package com.search;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebSearchService {
    
    public List<SearchResult> search(String query) {
        List<SearchResult> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        // Programming Languages
        if (lowerQuery.contains("java")) {
            results.add(new SearchResult("Java Tutorial - W3Schools", "https://www.w3schools.com/java/", "Learn Java programming from scratch. Java is used to develop mobile apps, web apps, desktop apps, and games."));
            results.add(new SearchResult("Java Documentation - Oracle", "https://docs.oracle.com/en/java/", "Official Java documentation. Comprehensive guides, API references, and tutorials for Java developers."));
            results.add(new SearchResult("Learn Java - GeeksforGeeks", "https://www.geeksforgeeks.org/java/", "Java tutorials covering basics to advanced concepts with examples and interview questions."));
            results.add(new SearchResult("Java Programming - Codecademy", "https://www.codecademy.com/learn/learn-java", "Interactive Java course to learn programming fundamentals and object-oriented concepts."));
        } else if (lowerQuery.contains("python")) {
            results.add(new SearchResult("Python Tutorial - W3Schools", "https://www.w3schools.com/python/", "Learn Python programming with interactive examples. Perfect for beginners and experienced developers."));
            results.add(new SearchResult("Welcome to Python.org", "https://www.python.org/", "Official Python website. Download Python, documentation, and community resources."));
            results.add(new SearchResult("Learn Python - Codecademy", "https://www.codecademy.com/learn/learn-python-3", "Interactive Python course covering syntax, data structures, and real-world applications."));
            results.add(new SearchResult("Python for Everybody", "https://www.py4e.com/", "Free Python course and book by Dr. Charles Severance covering fundamentals and data analysis."));
        } else if (lowerQuery.contains("javascript") || lowerQuery.contains("js")) {
            results.add(new SearchResult("JavaScript Tutorial - MDN", "https://developer.mozilla.org/en-US/docs/Web/JavaScript", "Complete JavaScript guide from Mozilla. Learn syntax, APIs, and modern JS features."));
            results.add(new SearchResult("JavaScript.info", "https://javascript.info/", "Modern JavaScript tutorial from basics to advanced topics with clear explanations."));
            results.add(new SearchResult("JavaScript - W3Schools", "https://www.w3schools.com/js/", "Interactive JavaScript tutorials covering DOM manipulation, events, and ES6 features."));
            results.add(new SearchResult("Learn JavaScript - freeCodeCamp", "https://www.freecodecamp.org/learn/javascript-algorithms-and-data-structures/", "Free certification course for JavaScript algorithms and data structures."));
        } else if (lowerQuery.contains("c++") || lowerQuery.contains("cpp")) {
            results.add(new SearchResult("C++ Tutorial - Learn C++", "https://www.learncpp.com/", "Comprehensive C++ tutorial covering basics to advanced OOP concepts."));
            results.add(new SearchResult("C++ Reference", "https://en.cppreference.com/", "Complete C++ language and library reference documentation."));
            results.add(new SearchResult("C++ Programming - GeeksforGeeks", "https://www.geeksforgeeks.org/c-plus-plus/", "C++ tutorials with examples, interview questions, and competitive programming tips."));
        } else if (lowerQuery.contains("react")) {
            results.add(new SearchResult("React Official Docs", "https://react.dev/", "Official React documentation. Learn components, hooks, and state management."));
            results.add(new SearchResult("React Tutorial - W3Schools", "https://www.w3schools.com/react/", "Interactive React tutorial covering JSX, components, and props."));
            results.add(new SearchResult("Learn React - freeCodeCamp", "https://www.freecodecamp.org/news/react-beginner-handbook/", "Comprehensive React beginner's handbook with projects and examples."));
        } else if (lowerQuery.contains("node") || lowerQuery.contains("nodejs")) {
            results.add(new SearchResult("Node.js Official", "https://nodejs.org/", "Official Node.js website. Download Node.js and access documentation."));
            results.add(new SearchResult("Node.js Tutorial - W3Schools", "https://www.w3schools.com/nodejs/", "Learn Node.js for backend development with practical examples."));
            results.add(new SearchResult("Node.js Guide - MDN", "https://developer.mozilla.org/en-US/docs/Learn/Server-side/Express_Nodejs", "Express/Node.js tutorial for building web applications."));
        }
        
        // Web Development
        else if (lowerQuery.contains("web") || lowerQuery.contains("html") || lowerQuery.contains("css")) {
            results.add(new SearchResult("MDN Web Docs", "https://developer.mozilla.org/", "Complete web development resource covering HTML, CSS, JavaScript, and APIs."));
            results.add(new SearchResult("Web Development - W3Schools", "https://www.w3schools.com/", "Free tutorials for HTML, CSS, JavaScript, and modern web technologies."));
            results.add(new SearchResult("freeCodeCamp - Web Development", "https://www.freecodecamp.org/", "Learn to code for free with certifications in web development and JavaScript."));
            results.add(new SearchResult("The Odin Project", "https://www.theodinproject.com/", "Full-stack web development curriculum with projects and community support."));
        }
        
        // Computer Science
        else if (lowerQuery.contains("algorithm")) {
            results.add(new SearchResult("Algorithms - MIT OpenCourseWare", "https://ocw.mit.edu/courses/introduction-to-algorithms/", "Introduction to Algorithms course materials from MIT with lectures and problem sets."));
            results.add(new SearchResult("Algorithms - Khan Academy", "https://www.khanacademy.org/computing/computer-science/algorithms", "Free algorithm course covering sorting, searching, and graph algorithms."));
            results.add(new SearchResult("Data Structures - GeeksforGeeks", "https://www.geeksforgeeks.org/data-structures/", "Comprehensive algorithms and data structures tutorials with code examples."));
            results.add(new SearchResult("VisuAlgo - Algorithm Visualizer", "https://visualgo.net/", "Interactive visualization of algorithms and data structures."));
        } else if (lowerQuery.contains("data structure")) {
            results.add(new SearchResult("Data Structures - GeeksforGeeks", "https://www.geeksforgeeks.org/data-structures/", "Complete guide to arrays, linked lists, trees, graphs, and hash tables."));
            results.add(new SearchResult("Data Structures - Programiz", "https://www.programiz.com/dsa", "Learn data structures with visual examples and code implementations."));
            results.add(new SearchResult("Data Structures Course - Coursera", "https://www.coursera.org/specializations/data-structures-algorithms", "University-level data structures and algorithms specialization."));
        } else if (lowerQuery.contains("machine learning") || lowerQuery.contains("ml")) {
            results.add(new SearchResult("Machine Learning - Coursera", "https://www.coursera.org/learn/machine-learning", "Andrew Ng's famous machine learning course covering fundamentals and applications."));
            results.add(new SearchResult("Machine Learning Crash Course - Google", "https://developers.google.com/machine-learning/crash-course", "Google's fast-paced ML course with TensorFlow APIs."));
            results.add(new SearchResult("ML Tutorial - GeeksforGeeks", "https://www.geeksforgeeks.org/machine-learning/", "Machine learning tutorials covering regression, classification, and neural networks."));
        } else if (lowerQuery.contains("ai") || lowerQuery.contains("artificial intelligence")) {
            results.add(new SearchResult("AI Fundamentals - MIT", "https://ocw.mit.edu/courses/artificial-intelligence/", "MIT's artificial intelligence course materials and lectures."));
            results.add(new SearchResult("Intro to AI - Udacity", "https://www.udacity.com/course/intro-to-artificial-intelligence--cs271", "Introduction to AI covering search, logic, and machine learning."));
            results.add(new SearchResult("AI - GeeksforGeeks", "https://www.geeksforgeeks.org/artificial-intelligence/", "Artificial intelligence tutorials and interview preparation."));
        }
        
        // Databases
        else if (lowerQuery.contains("sql") || lowerQuery.contains("database")) {
            results.add(new SearchResult("SQL Tutorial - W3Schools", "https://www.w3schools.com/sql/", "Learn SQL for database management with interactive examples."));
            results.add(new SearchResult("MySQL Tutorial", "https://www.mysqltutorial.org/", "Complete MySQL tutorial covering queries, joins, and optimization."));
            results.add(new SearchResult("PostgreSQL Tutorial", "https://www.postgresqltutorial.com/", "Learn PostgreSQL with practical examples and best practices."));
        } else if (lowerQuery.contains("mongodb")) {
            results.add(new SearchResult("MongoDB University", "https://university.mongodb.com/", "Free MongoDB courses and certifications from the official MongoDB team."));
            results.add(new SearchResult("MongoDB Tutorial - W3Schools", "https://www.w3schools.com/mongodb/", "Learn MongoDB NoSQL database with examples."));
            results.add(new SearchResult("MongoDB Docs", "https://www.mongodb.com/docs/", "Official MongoDB documentation and guides."));
        }
        
        // Tools & Frameworks
        else if (lowerQuery.contains("git") || lowerQuery.contains("github")) {
            results.add(new SearchResult("Git Tutorial - Atlassian", "https://www.atlassian.com/git/tutorials", "Comprehensive Git tutorials for version control and collaboration."));
            results.add(new SearchResult("GitHub Learning Lab", "https://lab.github.com/", "Interactive courses to learn Git and GitHub workflows."));
            results.add(new SearchResult("Learn Git Branching", "https://learngitbranching.js.org/", "Visual interactive Git tutorial for mastering branches and commits."));
        } else if (lowerQuery.contains("docker")) {
            results.add(new SearchResult("Docker Get Started", "https://docs.docker.com/get-started/", "Official Docker tutorial for containerization and deployment."));
            results.add(new SearchResult("Docker Tutorial - TutorialsPoint", "https://www.tutorialspoint.com/docker/", "Learn Docker from basics to advanced container management."));
            results.add(new SearchResult("Docker Curriculum", "https://docker-curriculum.com/", "Free comprehensive Docker tutorial for beginners."));
        } else if (lowerQuery.contains("kubernetes") || lowerQuery.contains("k8s")) {
            results.add(new SearchResult("Kubernetes Docs", "https://kubernetes.io/docs/tutorials/", "Official Kubernetes tutorials for container orchestration."));
            results.add(new SearchResult("Kubernetes Tutorial - TutorialsPoint", "https://www.tutorialspoint.com/kubernetes/", "Learn Kubernetes concepts and deployment strategies."));
            results.add(new SearchResult("Learn Kubernetes - Katacoda", "https://www.katacoda.com/courses/kubernetes", "Interactive Kubernetes scenarios in your browser."));
        }
        
        // Default fallback
        else {
            results.add(new SearchResult(query + " - Wikipedia", "https://en.wikipedia.org/wiki/" + query.replace(" ", "_"), "Learn more about " + query + " on Wikipedia, the free encyclopedia."));
            results.add(new SearchResult("Search " + query + " - Google", "https://www.google.com/search?q=" + query.replace(" ", "+"), "Find comprehensive information about " + query + " on Google Search."));
            results.add(new SearchResult(query + " Tutorial", "https://www.google.com/search?q=" + query.replace(" ", "+") + "+tutorial", "Find tutorials and learning resources for " + query + "."));
        }
        
        return results;
    }
}
