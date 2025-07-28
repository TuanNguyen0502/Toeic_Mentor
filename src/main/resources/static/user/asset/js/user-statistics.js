/**
 * Handles fetching and displaying user statistics history
 */
document.addEventListener('DOMContentLoaded', function() {
    // Get DOM elements
    const loadStatisticsBtn = document.getElementById('loadStatisticsBtn');
    const statisticsTable = document.getElementById('statisticsTable');
    const statisticsBody = document.getElementById('statisticsBody');
    const statisticsLoading = document.getElementById('statisticsLoading');
    const statisticsEmpty = document.getElementById('statisticsEmpty');
    const statisticsError = document.getElementById('statisticsError');

    // Add click event listener to the button
    if (loadStatisticsBtn) {
        loadStatisticsBtn.addEventListener('click', fetchStatisticsHistory);
    }

    /**
     * Fetches the user's statistics history from the API
     */
    function fetchStatisticsHistory() {
        // Show loading state
        showElement(statisticsLoading);
        hideElement(statisticsTable);
        hideElement(statisticsEmpty);
        hideElement(statisticsError);

        // Disable button during fetch
        if (loadStatisticsBtn) {
            loadStatisticsBtn.disabled = true;
            loadStatisticsBtn.innerHTML = 'Loading...';
        }

        // Make API call to fetch statistics history
        fetch('/statistics', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            credentials: 'same-origin'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch statistics');
            }
            return response.json();
        })
        .then(data => {
            // Process and display the statistics data
            displayStatisticsData(data);
        })
        .catch(error => {
            console.error('Error fetching statistics history:', error);
            showElement(statisticsError);
        })
        .finally(() => {
            // Reset loading states
            hideElement(statisticsLoading);
            if (loadStatisticsBtn) {
                loadStatisticsBtn.disabled = false;
                loadStatisticsBtn.innerHTML = 'Load Statistics';
            }
        });
    }

    /**
     * Displays statistics data in the table
     * @param {Array} data - Array of UserStatisticResponse objects
     */
    function displayStatisticsData(data) {
        // Clear existing table content
        statisticsBody.innerHTML = '';

        if (!data || data.length === 0) {
            // Show empty state message if no data
            showElement(statisticsEmpty);
            return;
        }

        // Sort data by createdAt date (newest first)
        data.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

        // Create and append table rows
        data.forEach(stat => {
            const row = document.createElement('tr');

            // Create row content
            row.innerHTML = `
                <td>${stat.createdAt}</td>
                <td>${stat.estimatedScore}</td>
                <td>${stat.minEstimatedScore} - ${stat.maxEstimatedScore}</td>
                <td>${stat.totalAnswers}</td>
                <td>${stat.totalCorrectAnswers}</td>
                <td>${stat.accuracy}%</td>
            `;

            // Add row to table
            statisticsBody.appendChild(row);
        });

        // Show the table
        showElement(statisticsTable);
    }

    /**
     * Helper function to show an element
     * @param {HTMLElement} element - Element to show
     */
    function showElement(element) {
        if (element) {
            element.style.display = 'block';
        }
    }

    /**
     * Helper function to hide an element
     * @param {HTMLElement} element - Element to hide
     */
    function hideElement(element) {
        if (element) {
            element.style.display = 'none';
        }
    }
});

