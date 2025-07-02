document.addEventListener('DOMContentLoaded', function() {
    // Existing unread count fetch
    fetch('/admin/notifications/count')
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.text();
        })
        .then(count => {
            document.getElementById('countUnreadNotification').textContent = count > 0 ? count : '';
        })
        .catch(error => {
            console.error('Error fetching notification count:', error);
        });

    let notificationsPage = {
        loading: false,
        lastCreatedAt: null,
        hasMore: true
    };

    function formatDateTime(dateTimeStr) {
        if (!dateTimeStr) return '';
        const date = new Date(dateTimeStr);
        if (isNaN(date.getTime())) return '';
        const pad = n => n.toString().padStart(2, '0');
        const hours = pad(date.getHours());
        const minutes = pad(date.getMinutes());
        const day = pad(date.getDate());
        const month = pad(date.getMonth() + 1);
        const year = date.getFullYear();
        return `${minutes}:${hours} ${day}/${month}/${year}`;
    }

    function renderNotifications(notifications) {
        const notificationList = document.getElementById('notificationList');
        const viewAllLi = notificationList.lastElementChild;
        notifications.forEach(notification => {
            const li = document.createElement('li');
            li.innerHTML = `
                <div class="noti-item w-full wg-user active">
                    <div class="flex-grow">
                        <div class="flex items-center justify-between">
                            <a href="/admin/notifications/${notification.id}" class="body-title">${notification.title}</a>
                            <div class="time">${formatDateTime(notification.createdAt)}</div>
                        </div>
                    </div>
                </div>
            `;
            notificationList.insertBefore(li, viewAllLi);
        });
    }

    function showLoading() {
        let loadingLi = document.getElementById('notification-loading');
        if (!loadingLi) {
            loadingLi = document.createElement('li');
            loadingLi.id = 'notification-loading';
            loadingLi.style.textAlign = 'center';
            loadingLi.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Loading...';
            const notificationList = document.getElementById('notificationList');
            const viewAllLi = notificationList.lastElementChild;
            notificationList.insertBefore(loadingLi, viewAllLi);
        }
    }

    function hideLoading() {
        const loadingLi = document.getElementById('notification-loading');
        if (loadingLi) loadingLi.remove();
    }

    function loadNotifications(initial = false) {
        if (notificationsPage.loading || !notificationsPage.hasMore) return;
        notificationsPage.loading = true;
        showLoading();
        let url = '/admin/notifications?pageSize=10';
        if (notificationsPage.lastCreatedAt) {
            // Use the ISO string as returned by the API, but remove milliseconds and timezone for compatibility with Spring's LocalDateTime
            let before = notificationsPage.lastCreatedAt;
            if (before.includes('T')) {
                before = before.split('.')[0];
                before = before.replace(/Z|\+\d{2}:\d{2}$/, '');
            }
            url += `&before=${encodeURIComponent(before)}`;
        }
        fetch(url)
            .then(response => response.json())
            .then(notifications => {
                if (initial) {
                    // Remove old notification items (except header and view all)
                    const notificationList = document.getElementById('notificationList');
                    const items = notificationList.querySelectorAll('li');
                    items.forEach((li, idx) => {
                        if (idx !== 0 && idx !== items.length - 1) li.remove();
                    });
                }
                renderNotifications(notifications);
                if (notifications.length > 0) {
                    notificationsPage.lastCreatedAt = notifications[notifications.length - 1].createdAt;
                }
                if (notifications.length < 10) {
                    notificationsPage.hasMore = false;
                }
                notificationsPage.loading = false;
                hideLoading();
            })
            .catch(error => {
                console.error('Error fetching notifications:', error);
                notificationsPage.loading = false;
                hideLoading();
            });
    }

    // Load notifications on dropdown open
    const notificationList = document.getElementById('notificationList');
    const dropdownButton = document.getElementById('dropdownMenuButton1');
    if (dropdownButton && notificationList) {
        dropdownButton.addEventListener('click', function() {
            notificationsPage = { loading: false, lastCreatedAt: null, hasMore: true };
            loadNotifications(true);
        });
        notificationList.addEventListener('scroll', function() {
            if (!notificationsPage.hasMore || notificationsPage.loading) return;
            // Check if scrolled to bottom
            if (notificationList.scrollTop + notificationList.clientHeight >= notificationList.scrollHeight - 10) {
                loadNotifications();
            }
        });
    }
});