const API = '/api';

const searchInput = document.getElementById('searchInput');
const searchInputMini = document.getElementById('searchInputMini');
const searchPage = document.getElementById('searchPage');
const resultsPage = document.getElementById('resultsPage');
const resultsGrid = document.getElementById('resultsGrid');
const resultsTitle = document.getElementById('resultsTitle');
const resultsSubtitle = document.getElementById('resultsSubtitle');
const loadingContainer = document.getElementById('loadingContainer');

searchInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter' && searchInput.value.trim()) {
        performSearch(searchInput.value.trim());
    }
});

async function performSearch(query) {
    try {
        searchPage.style.display = 'none';
        loadingContainer.style.display = 'block';
        
        const startTime = performance.now();
        const response = await fetch(`${API}/search?q=${encodeURIComponent(query)}`);
        const data = await response.json();
        const endTime = performance.now();
        const timeTaken = ((endTime - startTime) / 1000).toFixed(2);
        
        loadingContainer.style.display = 'none';
        
        showResults(data.results, data.answer, data.query, data.count, timeTaken);
    } catch (error) {
        console.error('Error:', error);
        loadingContainer.style.display = 'none';
        showError();
    }
}

function showResults(results, answer, query, count, time) {
    resultsPage.style.display = 'block';
    resultsPage.classList.add('fade-in');
    
    resultsTitle.textContent = `Results for "${query}"`;
    resultsSubtitle.textContent = `Found ${count} results in ${time} seconds`;
    searchInputMini.value = query;
    
    let htmlContent = '';
    
    // Show answer card WITHOUT image
    if (answer) {
        htmlContent += `
            <div class="answer-card fade-in">
                <div class="answer-badge">${answer.category}</div>
                <h3 class="answer-question">${answer.question}</h3>
                <p class="answer-text">${answer.answer}</p>
            </div>
        `;
    }
    
    if (!results || results.length === 0) {
        htmlContent += `
            <div class="no-results">
                <div class="no-results-icon">🔍</div>
                <h3>No results found</h3>
                <p>Try different keywords</p>
            </div>
        `;
    } else {
        htmlContent += results.map((result, index) => `
            <div class="result-card fade-in" style="animation-delay: ${index * 0.1}s">
                <div class="result-header">
                    <div>
                        <a href="${result.link}" target="_blank" class="result-title">
                            ${result.title}
                        </a>
                        <div class="result-url">${result.link}</div>
                    </div>
                </div>
                <div class="result-content">${result.snippet}</div>
            </div>
        `).join('');
    }
    
    resultsGrid.innerHTML = htmlContent;
}

function showError() {
    resultsPage.style.display = 'block';
    resultsGrid.innerHTML = `
        <div class="no-results">
            <div class="no-results-icon">⚠️</div>
            <h3>Something went wrong</h3>
            <p>Please try again</p>
        </div>
    `;
}

function backToSearch() {
    resultsPage.style.display = 'none';
    searchPage.style.display = 'flex';
    searchInput.value = '';
}

function searchFromMini() {
    const query = searchInputMini.value.trim();
    if (query) {
        performSearch(query);
    }
}
