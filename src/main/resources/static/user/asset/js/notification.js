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
            const notiClass = notification.isRead
                ? "notification-item"
                : "notification-item notification-unread";
            li.innerHTML = `
                <div class="${notiClass}">
                    <div class="notification-content">
                        <div class="notification-header">
                            <a href="${notification.urlToReportDetail}" class="notification-title">${notification.title}</a>
                            <span class="notification-time">${formatDateTime(notification.createdAt)}</span>
                        </div>
                        <div class="notification-message">${notification.message}</div>
                    </div>
                </div>
            `;
            // Attach click handler directly to the link
            const link = li.querySelector('.notification-title');
            link.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                const notiDiv = link.closest('.notification-item');
                const notificationId = notification.id;
                // Mark as read, then redirect
                fetch(`/notifications/${notificationId}/read`, { method: 'POST' })
                    .then(() => {
                        if (notiDiv.classList.contains('notification-unread')) {
                            notiDiv.classList.remove('notification-unread');
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
            loadingLi.className = 'notification-loading';
            loadingLi.innerHTML = '<div class="loading-spinner"></div><span>Loading...</span>';
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
                    document.getElementById('userCountUnreadNotification').textContent = '';
                    document.querySelectorAll('#userNotificationList .notification-item.notification-unread').forEach(div => {
                        div.classList.remove('notification-unread');
                    });
                });
        });
    }
});

(function() {
    const style = document.createElement('style');
    style.innerHTML = `
        /* New notification styles */
        #userNotificationList {
            max-height: 400px;
            overflow-y: auto;
            padding: 0;
            margin: 0;
            list-style-type: none;
            scrollbar-width: thin;
        }
        
        #userNotificationList::-webkit-scrollbar {
            width: 6px;
        }
        
        #userNotificationList::-webkit-scrollbar-thumb {
            background: #ccc;
            border-radius: 10px;
        }
        
        #userNotificationList::-webkit-scrollbar-track {
            background: #f1f1f1;
        }
        
        .notification-item {
            padding: 12px 16px;
            border-bottom: 1px solid #eaeaea;
            transition: all 0.2s ease;
            cursor: pointer;
        }
        
        .notification-item:hover {
            background-color: #f7f9fc;
        }
        
        .notification-content {
            display: flex;
            flex-direction: column;
            gap: 6px;
        }
        
        .notification-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
        }
        
        .notification-title {
            font-size: 14px;
            font-weight: 500;
            color: #2c3e50;
            text-decoration: none;
            margin-right: 10px;
            flex: 1;
        }
        
        .notification-title:hover {
            color: #3498db;
            text-decoration: underline;
        }
        
        .notification-time {
            font-size: 12px;
            color: #7f8c8d;
            white-space: nowrap;
        }
        
        .notification-message {
            font-size: 13px;
            color: #34495e;
            line-height: 1.4;
        }
        
        .notification-unread {
            background-color: rgba(52, 152, 219, 0.1);
            border-left: 3px solid #3498db;
        }
        
        .notification-unread .notification-title {
            font-weight: 600;
            color: #3498db;
        }
        
        .notification-loading {
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 12px;
            gap: 8px;
            color: #7f8c8d;
            font-size: 13px;
        }
        
        .loading-spinner {
            width: 18px;
            height: 18px;
            border: 2px solid rgba(52, 152, 219, 0.3);
            border-top: 2px solid #3498db;
            border-radius: 50%;
            animation: spin 0.8s linear infinite;
        }
        
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        
        #userMarkAllAsReadBtn {
            color: #3498db;
            background: none;
            border: none;
            padding: 8px 12px;
            cursor: pointer;
            font-size: 13px;
            font-weight: 500;
            transition: all 0.2s ease;
            text-decoration: none;
        }
        
        #userMarkAllAsReadBtn:hover {
            color: #2980b9;
            background-color: rgba(52, 152, 219, 0.1);
            border-radius: 4px;
        }
    `;
    document.head.appendChild(style);
})();
