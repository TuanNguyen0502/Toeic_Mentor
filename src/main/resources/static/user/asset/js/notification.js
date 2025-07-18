document.addEventListener('DOMContentLoaded', function() {
    // Fetch unread notification count
    fetch('/notifications/count')
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.text();
        })
        .then(count => {
            document.getElementById('userCountUnreadNotification').textContent = count > 0 ? count : '';
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
        return `${hours}:${minutes} - ${day}/${month}/${year}`;
    }

    function renderNotifications(notifications) {
        const notificationList = document.getElementById('userNotificationList');
        const viewAllLi = notificationList.lastElementChild;
        notifications.forEach(notification => {
            const li = document.createElement('li');
            const notiClass = notification.isRead ? "noti-item w-full wg-user" : "noti-item w-full wg-user unread";
            li.innerHTML = `
                <div class="${notiClass} active">
                    <div class="flex-grow">
                        <div class="flex items-center justify-between">
                            <a href="${notification.urlToReportDetail}" class="body-title">${notification.title}</a>
                            <div class="time">${formatDateTime(notification.createdAt)}</div>
                        </div>
                        <div class="text-tiny">${notification.message}</div>
                    </div>
                </div>
            `;
            // Attach click handler directly to the link
            const link = li.querySelector('.body-title');
            link.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                const notiDiv = link.closest('.noti-item');
                const notificationId = notification.id;
                // Mark as read, then redirect
                fetch(`/notifications/${notificationId}/read`, { method: 'POST' })
                    .then(() => {
                        if (notiDiv.classList.contains('unread')) {
                            notiDiv.classList.remove('unread');
                            const countElem = document.getElementById('userCountUnreadNotification');
                            let count = parseInt(countElem.textContent) || 0;
                            if (count > 0) countElem.textContent = count - 1 === 0 ? '' : (count - 1);
                        }
                        // Redirect to urlToReportDetail if present
                        // if (notification.urlToReportDetail) {
                        //     let url = notification.urlToReportDetail.replace(/[{}]/g, '');
                        //     window.location.href = url;
                        // }
                    });
            });
            notificationList.insertBefore(li, viewAllLi);
        });
    }

    function showLoading() {
        let loadingLi = document.getElementById('user-notification-loading');
        if (!loadingLi) {
            loadingLi = document.createElement('li');
            loadingLi.id = 'user-notification-loading';
            loadingLi.style.textAlign = 'center';
            loadingLi.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Loading...';
            const notificationList = document.getElementById('userNotificationList');
            const viewAllLi = notificationList.lastElementChild;
            notificationList.insertBefore(loadingLi, viewAllLi);
        }
    }

    function hideLoading() {
        const loadingLi = document.getElementById('user-notification-loading');
        if (loadingLi) loadingLi.remove();
    }

    function loadNotifications(initial = false) {
        if (notificationsPage.loading || !notificationsPage.hasMore) return;
        notificationsPage.loading = true;
        showLoading();
        let url = '/notifications?pageSize=10';
        if (notificationsPage.lastCreatedAt) {
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
                    const notificationList = document.getElementById('userNotificationList');
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
    const notificationList = document.getElementById('userNotificationList');
    const dropdownButton = document.getElementById('userDropdownNotificationBtn');
    if (dropdownButton && notificationList) {
        dropdownButton.addEventListener('click', function() {
            notificationsPage = { loading: false, lastCreatedAt: null, hasMore: true };
            loadNotifications(true);
        });
        notificationList.addEventListener('scroll', function() {
            if (!notificationsPage.hasMore || notificationsPage.loading) return;
            if (notificationList.scrollTop + notificationList.clientHeight >= notificationList.scrollHeight - 10) {
                loadNotifications();
            }
        });
    }

    // Mark all as read button handler
    const markAllBtn = document.getElementById('userMarkAllAsReadBtn');
    if (markAllBtn) {
        markAllBtn.addEventListener('click', function(e) {
            e.preventDefault();
            fetch('/notifications/read-all', { method: 'POST' })
                .then(() => {
                    document.getElementById('userCountUnreadNotification').textContent = '0';
                    document.querySelectorAll('#userNotificationList .noti-item.unread').forEach(div => {
                        div.classList.remove('unread');
                    });
                });
        });
    }
});

(function() {
    const style = document.createElement('style');
    style.innerHTML = `
        #userNotificationList .noti-item.unread {
            background-color: #f0f6ff;
            font-weight: bold;
        }
        #userNotificationList .noti-item.unread .body-title {
            color: #0d6efd;
        }
        #userNotificationList .noti-item.unread:hover {
            background-color: #e0eaff;
        }
    `;
    document.head.appendChild(style);
})();

