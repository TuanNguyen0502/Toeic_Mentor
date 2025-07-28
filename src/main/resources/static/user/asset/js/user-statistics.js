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

    // Delete modal elements
    const deleteModalElement = document.getElementById('deleteStatisticModal');
    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');

    // Initialize the modal using vanilla JS
    let deleteModal;

    // Variable to store the ID of the statistic to delete
    let statisticIdToDelete = null;

    // Add click event listener to the button
    if (loadStatisticsBtn) {
        loadStatisticsBtn.addEventListener('click', fetchStatisticsHistory);
    }

    // Add click event listener to the confirm delete button
    if (confirmDeleteBtn) {
        confirmDeleteBtn.addEventListener('click', confirmDelete);
    }

    // Initialize the Bootstrap modal once the page is fully loaded
    if (typeof bootstrap !== 'undefined' && deleteModalElement) {
        deleteModal = new bootstrap.Modal(deleteModalElement);
    } else {
        console.error('Bootstrap is not loaded or modal element not found');
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

        // Add header row for the action column if not already added
        const headerRow = document.querySelector('#statisticsTable thead tr');
        if (headerRow && headerRow.querySelectorAll('th').length === 6) {
            const actionHeader = document.createElement('th');
            actionHeader.textContent = 'Action';
            headerRow.appendChild(actionHeader);
        }

        // Create and append table rows
        data.forEach(stat => {
            const row = document.createElement('tr');

            // Get the ID from the statistic
            const statId = stat.id;

            // Create row content
            row.innerHTML = `
                <td>${stat.createdAt}</td>
                <td>${stat.estimatedScore}</td>
                <td>${stat.minEstimatedScore} - ${stat.maxEstimatedScore}</td>
                <td>${stat.totalAnswers}</td>
                <td>${stat.totalCorrectAnswers}</td>
                <td>${stat.accuracy}%</td>
                <td>
                    <button class="btn btn-danger btn-sm delete-btn" data-stat-id="${statId}">
                        <i class="feather-trash-2"></i> Delete
                    </button>
                </td>
            `;

            // Add row to table
            statisticsBody.appendChild(row);
        });

        // Now that the DOM is updated, add event listeners to the delete buttons
        document.querySelectorAll('.delete-btn').forEach(button => {
            button.addEventListener('click', function() {
                const statId = this.getAttribute('data-stat-id');
                showDeleteConfirmation(statId);
            });
        });

        // Show the table
        showElement(statisticsTable);
    }

    /**
     * Shows the delete confirmation modal
     * @param {string} statId - ID of the statistic to delete
     */
    function showDeleteConfirmation(statId) {
        statisticIdToDelete = statId;

        // Check if Bootstrap is loaded and modal is initialized
        if (typeof bootstrap !== 'undefined' && !deleteModal && deleteModalElement) {
            deleteModal = new bootstrap.Modal(deleteModalElement);
        }

        // Open the modal using jQuery as fallback if Bootstrap Modal isn't working
        if (deleteModal) {
            deleteModal.show();
        } else if (typeof $ !== 'undefined') {
            $(deleteModalElement).modal('show');
        } else {
            console.error("Neither Bootstrap nor jQuery is available for showing the modal");
            // Fallback to confirm dialog
            if (confirm('Are you sure you want to delete this statistics entry? This action cannot be undone.')) {
                confirmDelete();
            }
        }
    }

    /**
     * Confirms the deletion and calls the API
     */
    function confirmDelete() {
        if (!statisticIdToDelete) return;

        // Show loading state in the button
        confirmDeleteBtn.disabled = true;
        confirmDeleteBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Deleting...';

        console.log('Deleting statistic with ID:', statisticIdToDelete);

        // Call API to delete the statistic
        fetch(`/statistics/${statisticIdToDelete}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to delete statistic');
            }
            return response.text();
        })
        .then(message => {
            console.log('Delete response:', message);

            // Hide the modal
            if (deleteModal) {
                deleteModal.hide();
            } else if (typeof $ !== 'undefined') {
                $(deleteModalElement).modal('hide');
            }

            // Show success message
            alert('Statistic deleted successfully');

            // Reload statistics
            fetchStatisticsHistory();
        })
        .catch(error => {
            console.error('Error deleting statistic:', error);
            alert('Failed to delete statistic. Please try again.');
        })
        .finally(() => {
            // Reset button state
            confirmDeleteBtn.disabled = false;
            confirmDeleteBtn.innerHTML = 'Delete';

            // Reset the ID
            statisticIdToDelete = null;
        });
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
