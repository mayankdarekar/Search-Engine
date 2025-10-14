const API = '/api';

const searchInput = document.getElementById('searchInput');
const searchInputMini = document.getElementById('searchInputMini');
const searchPage = document.getElementById('searchPage');
const resultsPage = document.getElementById('resultsPage');
const resultsGrid = document.getElementById('resultsGrid');
const resultsTitle = document.getElementById('resultsTitle');
const resultsSubtitle = document.getElementById('resultsSubtitle');

searchInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter' && searchInput.value.trim()) {
        performSearch(searchInput.value.trim());
    }
});

async function performSearch(query) {
    try {
        searchInput.value = query;
        
        const startTime = performance.now();
        const response = await fetch(`${API}/search?q=${encodeURIComponent(query)}`);
        const data = await response.json();
        const endTime = performance.now();
        const timeTaken = ((endTime - startTime) / 1000).toFixed(2);
        
        showResults(data.results, data.query, data.count, timeTaken);
    } catch (error) {
        console.error('Error:', error);
        showError();
    }
}

function showResults(results, query, count, time) {
    searchPage.style.display = 'none';
    resultsPage.style.display = 'block';
    
    resultsTitle.textContent = `Results for "${query}"`;
    resultsSubtitle.textContent = `Found ${count} results in ${time} seconds`;
    searchInputMini.value = query;
    
    if (results.length === 0) {
        resultsGrid.innerHTML = `
            <div class="no-results">
                <div class="no-results-icon">🔍</div>
                <h3>No results found</h3>
                <p>Try different keywords</p>
            </div>
        `;
        return;
    }
    
    resultsGrid.innerHTML = results.map(result => `
        <div class="result-card">
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

function showError() {
    searchPage.style.display = 'none';
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
