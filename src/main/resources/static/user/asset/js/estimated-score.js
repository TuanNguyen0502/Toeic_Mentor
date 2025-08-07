/**
 * Handles fetching and displaying the user's estimated TOEIC score statistics
 */
document.addEventListener('DOMContentLoaded', function() {
    // Get the button and results container
    const fetchButton = document.getElementById('fetchEstimatedScoreBtn');
    const resultsContainer = document.getElementById('estimatedScoreResults');

    // Elements to display statistics
    const estimatedScore = document.getElementById('estimatedScore');
    const scoreRange = document.getElementById('scoreRange');
    const totalAnswers = document.getElementById('totalAnswers');
    const correctAnswers = document.getElementById('correctAnswers');
    const accuracy = document.getElementById('accuracy');

    // Add click event listener to the button
    if (fetchButton) {
        fetchButton.addEventListener('click', function() {
            fetchEstimatedScore('/statistics/estimated-score');
        });
    }

    // Automatically fetch the latest statistics when page loads
    fetchEstimatedScore('/statistics/latest');

    // Function to fetch the statistics data from the API
    function fetchEstimatedScore(endpoint) {
        // Show loading state
        if (fetchButton) {
            fetchButton.disabled = true;
            fetchButton.textContent = 'Loading...';
        }

        // Make API call to the statistics endpoint
        fetch(endpoint, {
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
            // Display the results
            estimatedScore.textContent = data.estimatedScore;
            scoreRange.textContent = data.minEstimatedScore + ' - ' + data.maxEstimatedScore;
            totalAnswers.textContent = data.totalAnswers;
            correctAnswers.textContent = data.totalCorrectAnswers;
            accuracy.textContent = data.accuracy;

            // Show the results container
            resultsContainer.style.display = 'block';
        })
        .catch(error => {
            console.error('Error fetching statistics:', error);
            // Don't show alert on initial page load, only when button is explicitly clicked
            if (endpoint.includes('estimated-score')) {
                alert('Failed to load your statistics. Please try again later.');
            }
        })
        .finally(() => {
            // Reset button state
            if (fetchButton) {
                fetchButton.disabled = false;
                fetchButton.textContent = 'View My Estimated TOEIC Score';
            }
        });
    }
});
