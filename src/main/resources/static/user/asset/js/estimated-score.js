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
        fetchButton.addEventListener('click', fetchEstimatedScore);
    }

    // Function to fetch the estimated score data from the API
    function fetchEstimatedScore() {
        // Show loading state
        fetchButton.disabled = true;
        fetchButton.textContent = 'Loading...';

        // Make API call to the statistics endpoint
        fetch('/statistics/estimated-score', {
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
            alert('Failed to load your statistics. Please try again later.');
        })
        .finally(() => {
            // Reset button state
            fetchButton.disabled = false;
            fetchButton.textContent = 'View My Estimated TOEIC Score';
        });
    }
});

