// Simple WebSocket handler for admin notifications
document.addEventListener('DOMContentLoaded', function() {
    // WebSocket connection
    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);
    
    // Get JWT token from cookie
    const token = document.cookie.split('; ').find(row => row.startsWith('jwt='))
        ?.split('=')[1] || '';

    // Connect to WebSocket
    stompClient.connect({ Authorization: "Bearer " + token }, function (frame) {
        console.log('WebSocket connected:', frame);
        
        // Subscribe to admin notifications
        stompClient.subscribe("/user/queue/admin/notifications", function (message) {
            try {
                const notification = JSON.parse(message.body);
                handleNewNotification(notification);
            } catch (error) {
                console.error('Error parsing notification:', error);
            }
        });
    }, function(error) {
        console.error('WebSocket connection error:', error);
    });

    // Handle new notification
    function handleNewNotification(notification) {
        // Update unread count
        const countElem = document.getElementById('countUnreadNotification');
        let currentCount = parseInt(countElem.textContent) || 0;
        countElem.textContent = currentCount + 1;

        // Add notification to dropdown if open
        const notificationList = document.getElementById('notificationList');
        if (notificationList) {
            const newLi = createNotificationElement(notification);
            notificationList.insertBefore(newLi, notificationList.children[1]); // Insert after header
        }

        // Show toast notification
        showToast(notification);
    }

    // Create notification element
    function createNotificationElement(notification) {
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

        // Add click handler
        const link = li.querySelector('.body-title');
        link.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            const notiDiv = link.closest('.noti-item');
            const notificationId = notification.id;
            
            fetch(`/admin/notifications/${notificationId}/read`, { method: 'POST' })
                .then(() => {
                    if (notiDiv.classList.contains('unread')) {
                        notiDiv.classList.remove('unread');
                        const countElem = document.getElementById('countUnreadNotification');
                        let count = parseInt(countElem.textContent) || 0;
                        if (count > 0) countElem.textContent = count - 1 === 0 ? '' : (count - 1);
                    }
                    if (notification.urlToReportDetail) {
                        let url = notification.urlToReportDetail.replace(/[{}]/g, '');
                        window.location.href = url;
                    }
                });
        });

        return li;
    }

    // Show toast notification
    function showToast(notification) {
        const toast = document.createElement('div');
        toast.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            width: 350px;
            background: white;
            border: 1px solid #ddd;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            z-index: 9999;
            animation: slideIn 0.3s ease-out;
        `;
        
        toast.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center; padding: 12px 16px; border-bottom: 1px solid #eee; background: #f8f9fa; border-radius: 8px 8px 0 0;">
                <strong>${notification.title}</strong>
                <button onclick="this.parentElement.parentElement.remove()" style="background: none; border: none; font-size: 18px; cursor: pointer;">×</button>
            </div>
            <div style="padding: 12px 16px; color: #333;">
                ${notification.message}
            </div>
        `;

        document.body.appendChild(toast);

        // Auto remove after 5 seconds
        setTimeout(() => {
            if (toast.parentElement) {
                toast.remove();
            }
        }, 5000);
    }

    // Format date time helper
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

    // Add CSS for animation
    const style = document.createElement('style');
    style.innerHTML = `
        @keyframes slideIn {
            from { transform: translateX(100%); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }
    `;
    document.head.appendChild(style);
}); 