/**
 * Study Streak Calendar Visualization for Profile Page
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize calendar only if the studyCalendar element exists
    const studyCalendarEl = document.getElementById('studyCalendar');
    if (studyCalendarEl) {
        loadStudyStreakData();
    }
});

/**
 * Fetch study streak data from the server
 */
function loadStudyStreakData() {
    const streakLoading = document.getElementById('streakLoading');
    const streakError = document.getElementById('streakError');

    // Show loading indicator
    streakLoading.style.display = 'block';

    fetch('/study-streaks/detail')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Hide loading indicator
            streakLoading.style.display = 'none';

            // Update streak information
            updateStreakInfo(data);

            // Render calendar with study history
            renderSimpleStudyCalendar(data.histories);

            // Display achievements
            renderAchievements(data.achievements);
        })
        .catch(error => {
            console.error('Error fetching study streak data:', error);
            streakLoading.style.display = 'none';
            streakError.style.display = 'block';
        });
}

/**
 * Update the streak information on the page
 */
function updateStreakInfo(data) {
    const currentStreakEl = document.getElementById('currentStreak');
    const maxStreakEl = document.getElementById('maxStreak');
    const lastStudyDateEl = document.getElementById('lastStudyDate');

    if (currentStreakEl) {
        currentStreakEl.textContent = `Current Streak: ${data.currentStreak}`;
    }

    if (maxStreakEl) {
        maxStreakEl.textContent = `Max Streak: ${data.maxStreak}`;
    }

    if (lastStudyDateEl && data.lastStudyDate) {
        lastStudyDateEl.textContent = data.lastStudyDate;
    }
}

/**
 * Generate a simple calendar with study session markers
 * @param {Object} histories - Map of study sessions with start and end times
 */
function renderSimpleStudyCalendar(histories) {
    const calendarEl = document.getElementById('studyCalendar');
    if (!calendarEl) return;

    // Clear previous content
    calendarEl.innerHTML = '';

    // Get current date to determine which month to display
    const today = new Date();
    let currentMonth = today.getMonth();
    let currentYear = today.getFullYear();

    // Create map of days with study sessions
    const studyDays = new Map();

    // Process the study session histories
    if (histories) {
        console.log("Processing study histories:", histories);
        Object.entries(histories).forEach(([startTime, endTime]) => {
            // Process start date
            const startDate = new Date(startTime);

            // Process end date - if null, use current date
            const endDate = endTime ? new Date(endTime) : new Date();

            // Add all days between start and end dates (inclusive)
            const currentDate = new Date(startDate);

            // Loop through each day in the study period
            while (currentDate <= endDate) {
                const dateKey = `${currentDate.getFullYear()}-${currentDate.getMonth() + 1}-${currentDate.getDate()}`;
                console.log(`Adding study day: ${dateKey} from period ${startTime} to ${endTime || 'present'}`);
                studyDays.set(dateKey, true);

                // Move to the next day
                currentDate.setDate(currentDate.getDate() + 1);
            }

            // If endTime is null, explicitly make sure today is added
            if (endTime === null) {
                const today = new Date();
                const todayKey = `${today.getFullYear()}-${today.getMonth() + 1}-${today.getDate()}`;
                console.log(`Explicitly adding today: ${todayKey} because endTime is null`);
                studyDays.set(todayKey, true);
            }
        });
    }

    // Create calendar container
    const calendarContainer = document.createElement('div');
    calendarContainer.className = 'simple-calendar';

    // Create navigation controls
    const navContainer = document.createElement('div');
    navContainer.className = 'calendar-nav d-flex justify-content-between align-items-center mb-2';

    const prevBtn = document.createElement('button');
    prevBtn.innerHTML = '&laquo; Prev';
    prevBtn.className = 'btn btn-sm btn-outline-primary';
    prevBtn.addEventListener('click', () => {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        renderMonth(currentYear, currentMonth);
    });

    const monthYearDisplay = document.createElement('h5');
    monthYearDisplay.className = 'mb-0';

    const nextBtn = document.createElement('button');
    nextBtn.innerHTML = 'Next &raquo;';
    nextBtn.className = 'btn btn-sm btn-outline-primary';
    nextBtn.addEventListener('click', () => {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        renderMonth(currentYear, currentMonth);
    });

    navContainer.appendChild(prevBtn);
    navContainer.appendChild(monthYearDisplay);
    navContainer.appendChild(nextBtn);
    calendarContainer.appendChild(navContainer);

    // Create calendar grid
    const calendarGrid = document.createElement('div');
    calendarGrid.className = 'calendar-grid';
    calendarContainer.appendChild(calendarGrid);

    // Add calendar to DOM
    calendarEl.appendChild(calendarContainer);

    // Function to render a specific month
    function renderMonth(year, month) {
        // Update month/year display
        const monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
                           'July', 'August', 'September', 'October', 'November', 'December'];
        monthYearDisplay.textContent = `${monthNames[month]} ${year}`;

        // Clear grid
        calendarGrid.innerHTML = '';

        // Add day headers
        const dayNames = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
        dayNames.forEach(day => {
            const dayHeader = document.createElement('div');
            dayHeader.className = 'calendar-day-header';
            dayHeader.textContent = day;
            calendarGrid.appendChild(dayHeader);
        });

        // Get first day of the month and total days
        const firstDayOfMonth = new Date(year, month, 1).getDay();
        const daysInMonth = new Date(year, month + 1, 0).getDate();

        // Add empty cells for days before the 1st of the month
        for (let i = 0; i < firstDayOfMonth; i++) {
            const emptyDay = document.createElement('div');
            emptyDay.className = 'calendar-day empty';
            calendarGrid.appendChild(emptyDay);
        }

        // Add cells for each day of the month
        for (let day = 1; day <= daysInMonth; day++) {
            const dayCell = document.createElement('div');
            dayCell.className = 'calendar-day';
            dayCell.textContent = day;

            // Check if this day has study sessions
            // Format: YYYY-MM-DD (to ensure proper matching)
            const dateKey = `${year}-${month + 1}-${day}`;
            if (studyDays.has(dateKey)) {
                dayCell.classList.add('has-study');
                console.log(`Highlighted day ${day} with key ${dateKey}`);
            }

            // Highlight today
            if (year === today.getFullYear() && month === today.getMonth() && day === today.getDate()) {
                dayCell.classList.add('today');
            }

            calendarGrid.appendChild(dayCell);
        }
    }

    // Initial render of current month
    renderMonth(currentYear, currentMonth);

    // Add CSS for the calendar
    const styleElement = document.createElement('style');
    styleElement.textContent = `
        .simple-calendar {
            font-family: Arial, sans-serif;
        }
        .calendar-grid {
            display: grid;
            grid-template-columns: repeat(7, 1fr);
            gap: 2px;
        }
        .calendar-day-header {
            text-align: center;
            font-weight: bold;
            padding: 5px;
            background-color: #f8f9fa;
        }
        .calendar-day {
            height: 40px;
            text-align: center;
            padding: 5px;
            border: 1px solid #dee2e6;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: default;
        }
        .calendar-day.empty {
            background-color: #f8f9fa;
            border: 1px solid #f0f0f0;
        }
        .calendar-day.has-study {
            background-color: #cfe2ff;
            color: #0d6efd;
            font-weight: bold;
        }
        .calendar-day.today {
            border: 2px solid #0d6efd;
        }
        .calendar-day.has-study.today {
            background-color: #cfe2ff;
            border: 2px solid #0d6efd;
            color: #0d6efd;
            font-weight: bold;
        }
    `;
    document.head.appendChild(styleElement);
}

/**
 * Render the achievements section
 * @param {Object} achievements - Map of achievement levels and descriptions
 */
function renderAchievements(achievements) {
    const achievementsEl = document.getElementById('achievements');
    if (!achievementsEl || !achievements) return;

    // Clear previous content
    achievementsEl.innerHTML = '';

    // If no achievements, show a message
    if (Object.keys(achievements).length === 0) {
        achievementsEl.innerHTML = '<div class="alert alert-info">No achievements yet. Keep studying to earn achievements!</div>';
        return;
    }

    // Create a list of achievements
    const list = document.createElement('ul');
    list.className = 'list-group';

    Object.entries(achievements).forEach(([level, description]) => {
        const item = document.createElement('li');
        item.className = 'list-group-item d-flex justify-content-between align-items-center';

        // Create badge for the achievement level
        const badge = document.createElement('span');
        badge.className = 'badge bg-warning rounded-pill';
        badge.textContent = `Level ${level}`;

        // Add the description and badge
        item.textContent = description;
        item.appendChild(badge);

        list.appendChild(item);
    });

    achievementsEl.appendChild(list);
}
