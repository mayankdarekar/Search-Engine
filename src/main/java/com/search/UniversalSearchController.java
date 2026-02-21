package com.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UniversalSearchController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam(name = "q") String query,
                                      @RequestParam(name = "page", defaultValue = "1") int page) {
        String q = query.trim();
        if (q.isEmpty()) return emptyResponse(q);

        ExecutorService pool = Executors.newFixedThreadPool(8);
        var wikiF  = CompletableFuture.supplyAsync(() -> searchWikipedia(q), pool);
        var ddgF   = CompletableFuture.supplyAsync(() -> searchDuckDuckGo(q), pool);
        var ghF    = CompletableFuture.supplyAsync(() -> searchGitHub(q), pool);
        var soF    = CompletableFuture.supplyAsync(() -> searchStackOverflow(q), pool);
        var hnF    = CompletableFuture.supplyAsync(() -> searchHackerNews(q), pool);
        var redF   = CompletableFuture.supplyAsync(() -> searchReddit(q), pool);
        var bookF  = CompletableFuture.supplyAsync(() -> searchOpenLibrary(q), pool);
        var newsF  = CompletableFuture.supplyAsync(() -> searchWikiNews(q), pool);

        List<Map<String, Object>> all = new ArrayList<>();
        for (var f : List.of(wikiF, ddgF, ghF, soF, hnF, redF, bookF, newsF)) {
            try { var r = f.get(8, TimeUnit.SECONDS); if (r != null) all.addAll(r); }
            catch (Exception ignored) {}
        }
        pool.shutdown();

        // Score every result dynamically based on query relevance
        String[] terms = q.toLowerCase().split("\\s+");
        for (var r : all) {
            r.put("score", scoreResult(r, terms, q));
        }

        // Sort by score descending
        all.sort((a, b) -> Double.compare(
                (double) b.getOrDefault("score", 0.0),
                (double) a.getOrDefault("score", 0.0)));

        // Deduplicate by URL, preserve score order
        Set<String> seen = new LinkedHashSet<>();
        List<Map<String, Object>> unique = new ArrayList<>();
        for (var r : all) {
            String url = String.valueOf(r.getOrDefault("url", ""));
            if (!url.isBlank() && seen.add(url)) {
                r.remove("score"); // clean up internal field
                unique.add(r);
            }
        }

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("query", q);
        resp.put("total", unique.size());
        resp.put("results", unique);
        return resp;
    }

    // ─── RELEVANCE SCORER ───────────────────────────────────────────────────────
    private double scoreResult(Map<String, Object> r, String[] terms, String fullQuery) {
        String title   = ((String) r.getOrDefault("title",   "")).toLowerCase();
        String snippet = ((String) r.getOrDefault("snippet", "")).toLowerCase();
        String badge   = ((String) r.getOrDefault("badge",   "")).toLowerCase();
        String fq      = fullQuery.toLowerCase();
        double score   = 0;

        // Exact full query match in title → massive boost
        if (title.equals(fq))              score += 100;
        else if (title.contains(fq))       score += 60;

        // Exact full query in snippet
        if (snippet.contains(fq))          score += 30;

        // Each individual term match
        for (String term : terms) {
            if (term.length() < 2) continue;
            if (title.contains(term))      score += 15;
            if (snippet.contains(term))    score += 5;
        }

        // Term density in snippet (avoids padding)
        if (!snippet.isEmpty()) {
            long hits = Arrays.stream(terms).filter(snippet::contains).count();
            score += (hits * 10.0) / terms.length;
        }

        // Source authority boosts based on query type
        boolean isTech    = containsAny(fq, "algorithm","code","programming","software","api","function","data structure","language","framework","library","bug","error","stack","queue","tree","graph","sort","search","binary","java","python","javascript","react","node","spring");
        boolean isNews    = containsAny(fq, "news","latest","today","2024","2025","update","recent","current","war","election","president","minister","economy","market","price");
        boolean isBook    = containsAny(fq, "book","novel","author","fiction","nonfiction","read","literature","publish","biography");
        boolean isDiscuss = containsAny(fq, "how","why","what","explain","difference","vs","versus","best","recommend","opinion","reddit","forum","discuss","advice","help","should i");
        boolean isSci     = containsAny(fq, "science","physics","chemistry","biology","math","theorem","equation","theory","research","study","paper","quantum","relativity","evolution","dna");

        if (isTech) {
            if (badge.equals("stackoverflow"))  score += 25;
            if (badge.equals("github"))         score += 20;
            if (badge.equals("hackernews"))     score += 15;
            if (badge.equals("wiki"))           score += 10;
        }
        if (isNews) {
            if (badge.equals("hackernews"))     score += 25;
            if (badge.equals("reddit"))         score += 20;
            if (badge.equals("news"))           score += 20;
            if (badge.equals("ddg"))            score += 10;
        }
        if (isBook) {
            if (badge.equals("books"))          score += 35;
            if (badge.equals("wiki"))           score += 10;
        }
        if (isDiscuss) {
            if (badge.equals("reddit"))         score += 30;
            if (badge.equals("stackoverflow"))  score += 20;
            if (badge.equals("hackernews"))     score += 15;
        }
        if (isSci) {
            if (badge.equals("wiki"))           score += 30;
            if (badge.equals("books"))          score += 15;
            if (badge.equals("hackernews"))     score += 10;
        }

        // Generic wiki boost for anything encyclopedic
        if (!isTech && !isNews && !isBook && !isDiscuss) {
            if (badge.equals("wiki"))           score += 15;
            if (badge.equals("ddg"))            score += 10;
        }

        // Penalize very short snippets (likely low quality)
        if (snippet.length() < 30)             score -= 10;

        return score;
    }

    private boolean containsAny(String text, String... keywords) {
        for (String k : keywords) if (text.contains(k)) return true;
        return false;
    }

    // ─── SOURCES ────────────────────────────────────────────────────────────────
    private List<Map<String, Object>> searchWikipedia(String q) {
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            String url = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="
                    + enc(q) + "&format=json&srlimit=10&srprop=snippet";
            for (JsonNode hit : get(url).path("query").path("search")) {
                String t = hit.path("title").asText();
                results.add(result(t, "https://en.wikipedia.org/wiki/" + enc(t.replace(" ", "_")),
                        stripTags(hit.path("snippet").asText()), "Wikipedia", "wiki"));
            }
        } catch (Exception ignored) {}
        return results;
    }

    private List<Map<String, Object>> searchDuckDuckGo(String q) {
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            JsonNode root = get("https://api.duckduckgo.com/?q=" + enc(q) + "&format=json&no_html=1&skip_disambig=1&no_redirect=1");
            if (!root.path("AbstractText").asText().isBlank())
                results.add(result(root.path("Heading").asText(q), root.path("AbstractURL").asText(),
                        root.path("AbstractText").asText(), "DuckDuckGo", "ddg"));
            for (JsonNode t : root.path("RelatedTopics")) {
                String txt = t.path("Text").asText(), fu = t.path("FirstURL").asText();
                if (!txt.isBlank() && !fu.isBlank())
                    results.add(result(txt.length() > 70 ? txt.substring(0, 70) + "…" : txt,
                            fu, txt, "DuckDuckGo", "ddg"));
            }
            if (!root.path("Definition").asText().isBlank())
                results.add(result("Definition: " + q, root.path("DefinitionURL").asText(),
                        root.path("Definition").asText(),
                        root.path("DefinitionSource").asText("Dictionary"), "ddg"));
        } catch (Exception ignored) {}
        return results;
    }

    private List<Map<String, Object>> searchGitHub(String q) {
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            HttpHeaders h = new HttpHeaders();
            h.set("Accept", "application/vnd.github+json");
            h.set("User-Agent", "SearchHub/2.0");
            JsonNode root = mapper.readTree(restTemplate.exchange(
                    "https://api.github.com/search/repositories?q=" + enc(q) + "&sort=stars&order=desc&per_page=8",
                    HttpMethod.GET, new HttpEntity<>(h), String.class).getBody());
            for (JsonNode item : root.path("items")) {
                String lang = item.path("language").asText("");
                results.add(result(item.path("full_name").asText(), item.path("html_url").asText(),
                        item.path("description").asText("No description")
                                + (lang.isBlank() ? "" : " · " + lang)
                                + " · ⭐ " + formatNum(item.path("stargazers_count").asText("0")),
                        "GitHub", "github"));
            }
        } catch (Exception ignored) {}
        return results;
    }

    private List<Map<String, Object>> searchStackOverflow(String q) {
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            for (JsonNode item : get("https://api.stackexchange.com/2.3/search/advanced?order=desc&sort=relevance&q="
                    + enc(q) + "&site=stackoverflow&pagesize=8&filter=default").path("items")) {
                String tags = "";
                for (JsonNode t : item.path("tags")) tags += t.asText() + " ";
                results.add(result(item.path("title").asText(), item.path("link").asText(),
                        (item.path("is_answered").asBoolean() ? "✓ Answered" : "Unanswered")
                                + " · " + item.path("answer_count").asInt() + " answers"
                                + " · " + item.path("score").asInt() + " votes · " + tags.trim(),
                        "Stack Overflow", "stackoverflow"));
            }
        } catch (Exception ignored) {}
        return results;
    }

    private List<Map<String, Object>> searchHackerNews(String q) {
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            for (JsonNode hit : get("https://hn.algolia.com/api/v1/search?query=" + enc(q) + "&tags=story&hitsPerPage=8").path("hits")) {
                String url = hit.path("url").asText();
                if (url.isBlank()) url = "https://news.ycombinator.com/item?id=" + hit.path("objectID").asText();
                results.add(result(hit.path("title").asText(), url,
                        "👤 " + hit.path("author").asText() + " · " + hit.path("points").asInt()
                                + " points · 💬 " + hit.path("num_comments").asInt() + " comments",
                        "Hacker News", "hackernews"));
            }
        } catch (Exception ignored) {}
        return results;
    }

    private List<Map<String, Object>> searchReddit(String q) {
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            HttpHeaders h = new HttpHeaders(); h.set("User-Agent", "SearchHub/2.0");
            JsonNode root = mapper.readTree(restTemplate.exchange(
                    "https://www.reddit.com/search.json?q=" + enc(q) + "&sort=relevance&limit=8&t=all",
                    HttpMethod.GET, new HttpEntity<>(h), String.class).getBody());
            for (JsonNode child : root.path("data").path("children")) {
                JsonNode d = child.path("data");
                String self = d.path("selftext").asText("");
                if (self.length() > 200) self = self.substring(0, 200) + "…";
                results.add(result(d.path("title").asText(),
                        "https://www.reddit.com" + d.path("permalink").asText(),
                        (self.isBlank() ? "" : self + " · ")
                                + d.path("subreddit_name_prefixed").asText()
                                + " · ⬆ " + formatNum(String.valueOf(d.path("score").asInt())),
                        "Reddit", "reddit"));
            }
        } catch (Exception ignored) {}
        return results;
    }

    private List<Map<String, Object>> searchOpenLibrary(String q) {
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            for (JsonNode doc : get("https://openlibrary.org/search.json?q=" + enc(q) + "&limit=6").path("docs")) {
                String t = doc.path("title").asText(); if (t.isBlank()) continue;
                String author = doc.path("author_name").isArray() && doc.path("author_name").size() > 0
                        ? doc.path("author_name").get(0).asText("") : "";
                results.add(result(t, "https://openlibrary.org" + doc.path("key").asText(),
                        (author.isBlank() ? "" : "by " + author + " · ") + "Published " + doc.path("first_publish_year").asText("?"),
                        "Open Library", "books"));
            }
        } catch (Exception ignored) {}
        return results;
    }

    private List<Map<String, Object>> searchWikiNews(String q) {
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            for (JsonNode hit : get("https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch="
                    + enc(q + " news OR 2024 OR 2025") + "&format=json&srlimit=5&srprop=snippet")
                    .path("query").path("search")) {
                String t = hit.path("title").asText();
                results.add(result(t, "https://en.wikipedia.org/wiki/" + enc(t.replace(" ", "_")),
                        stripTags(hit.path("snippet").asText()), "Wikipedia News", "news"));
            }
        } catch (Exception ignored) {}
        return results;
    }

    // ─── HELPERS ────────────────────────────────────────────────────────────────
    private JsonNode get(String url) throws Exception {
        HttpHeaders h = new HttpHeaders();
        h.set("User-Agent", "SearchHub/2.0"); h.set("Accept", "application/json");
        return mapper.readTree(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(h), String.class).getBody());
    }
    private Map<String, Object> result(String title, String url, String snippet, String source, String badge) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("title", title); m.put("url", url); m.put("snippet", snippet);
        m.put("source", source); m.put("badge", badge); return m;
    }
    private static String stripTags(String s) { return s == null ? "" : s.replaceAll("<[^>]*>", ""); }
    private static String enc(String s) { return URLEncoder.encode(s, StandardCharsets.UTF_8); }
    private static String formatNum(String s) {
        try { long n = Long.parseLong(s); return n >= 1000 ? String.format("%.1fk", n / 1000.0) : s; }
        catch (Exception e) { return s; }
    }
    private Map<String, Object> emptyResponse(String q) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("query", q); r.put("total", 0); r.put("results", List.of()); return r;
    }
}
