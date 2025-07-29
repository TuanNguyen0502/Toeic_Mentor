/**
 * Handles fetching and displaying test statistics
 */
document.addEventListener('DOMContentLoaded', function() {
    // Elements to display statistics
    const totalTests = document.getElementById('totalTests');
    const averageScore = document.getElementById('averageScore');
    const highestScore = document.getElementById('highestScore');
    const totalTimeSpent = document.getElementById('totalTimeSpent');

    // Function to fetch test statistics
    function fetchTestStatistics() {
        fetch('/tests/statistics', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            credentials: 'same-origin'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch test statistics');
            }
            return response.json();
        })
        .then(data => {
            // Display the statistics
            if (totalTests) totalTests.textContent = data.totalTests || 0;
            if (averageScore) averageScore.textContent = (data.averageScore || 0) + '%';
            if (highestScore) highestScore.textContent = (data.highestScore || 0) + '%';
            if (totalTimeSpent) totalTimeSpent.textContent = data.totalTimeSpent || 0;
        })
        .catch(error => {
            console.error('Error fetching test statistics:', error);
            // Set default values on error
            if (totalTests) totalTests.textContent = '0';
            if (averageScore) averageScore.textContent = '0%';
            if (highestScore) highestScore.textContent = '0%';
            if (totalTimeSpent) totalTimeSpent.textContent = '0';
        });
    }

    // Fetch statistics when page loads
    fetchTestStatistics();

    // Refresh statistics every 5 minutes
    setInterval(fetchTestStatistics, 5 * 60 * 1000);
}); 