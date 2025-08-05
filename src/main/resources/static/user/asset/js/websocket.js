// Thêm đoạn kiểm tra ngay đầu file để đảm bảo script chỉ chạy logic khởi tạo một lần duy nhất.
// Chúng ta sử dụng một biến toàn cục (window) để cờ này tồn tại qua các lần script được nạp lại.
if (window.isUserWebSocketInitialized) {
    console.log('User WebSocket has already been initialized. Skipping.');
} else {
    // Đặt cờ để đánh dấu là đã khởi tạo.
    window.isUserWebSocketInitialized = true;

    document.addEventListener('DOMContentLoaded', function() {
        // WebSocket connection
        const socket = new SockJS('/ws');
        const stompClient = Stomp.over(socket);

        // Lưu stompClient vào biến toàn cục để có thể truy cập nếu cần (ví dụ: để ngắt kết nối)
        window.userStompClient = stompClient;

        // Get JWT token from cookie
        const token = document.cookie.split('; ').find(row => row.startsWith('jwt='))
            ?.split('=')[1] || '';

        // Connect to WebSocket
        stompClient.connect({ Authorization: "Bearer " + token }, function (frame) {
            console.log('User WebSocket connected:', frame);

            // Subscribe to user notifications
            stompClient.subscribe("/user/queue/notifications", function (message) {
                try {
                    const notification = JSON.parse(message.body);
                    handleNewNotification(notification);
                } catch (error) {
                    console.error('Error parsing notification:', error);
                }
            });
        }, function(error) {
            console.error('User WebSocket connection error:', error);
            // Nếu kết nối lỗi, reset cờ để có thể thử lại ở lần tải trang sau
            window.isUserWebSocketInitialized = false;
        });

        // Handle new notification
        function handleNewNotification(notification) {
            // Update unread count
            const countElem = document.getElementById('userCountUnreadNotification');
            // Đảm bảo rằng chúng ta lấy giá trị số hợp lệ, nếu rỗng thì là 0
            let currentCount = parseInt(countElem.textContent || '0', 10);
            countElem.textContent = currentCount + 1;

            // Add notification to dropdown if open
            const notificationList = document.getElementById('userNotificationList');
            if (notificationList) {
                const newLi = createNotificationElement(notification);
                // Chèn vào sau thẻ <li> chứa tiêu đề "Notifications"
                notificationList.insertBefore(newLi, notificationList.children[1]);
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
                            <a href="#" class="body-title">${notification.title}</a>
                            <div class="time">${formatDateTime(notification.createdAt)}</div>
                        </div>
                        <div class="text-tiny">${notification.message}</div>
                    </div>
                </div>
            `;

            // Add click handler to mark as read
            const link = li.querySelector('.body-title');
            link.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();

                const notiDiv = link.closest('.noti-item');
                // Chỉ xử lý khi thông báo chưa đọc
                if (notiDiv.classList.contains('unread')) {
                    const notificationId = notification.id;
                    fetch(`/notifications/${notificationId}/read`, { method: 'POST' })
                        .then(response => {
                            if (response.ok) {
                                notiDiv.classList.remove('unread');
                                const countElem = document.getElementById('userCountUnreadNotification');
                                let count = parseInt(countElem.textContent || '0', 10);
                                if (count > 0) {
                                    countElem.textContent = count - 1 === 0 ? '' : (count - 1);
                                }
                            }
                        });
                }
            });

            return li;
        }

        // Show toast notification
        function showToast(notification) {
            const toastContainer = document.body;
            const toast = document.createElement('div');
            toast.className = 'custom-toast'; // Sử dụng class để dễ dàng tạo style

            toast.innerHTML = `
                <div class="custom-toast-header">
                    <strong>${notification.title}</strong>
                    <button onclick="this.closest('.custom-toast').remove()" class="custom-toast-close">&times;</button>
                </div>
                <div class="custom-toast-body">
                    ${notification.message}
                </div>
            `;

            toastContainer.appendChild(toast);

            // Auto remove after 5 seconds
            setTimeout(() => {
                toast.classList.add('fade-out');
                toast.addEventListener('transitionend', () => toast.remove());
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
            return `${hours}:${minutes} - ${day}/${month}/${year}`;
        }

        // Add CSS for toast and animation
        // Đặt CSS vào trong JS để đảm bảo nó luôn tồn tại cùng với logic toast
        const style = document.createElement('style');
        style.innerHTML = `
            .custom-toast {
                position: fixed;
                top: 20px;
                right: 20px;
                width: 350px;
                background: white;
                border: 1px solid #ddd;
                border-radius: 8px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                z-index: 9999;
                transform: translateX(100%);
                opacity: 0;
                transition: transform 0.3s ease-out, opacity 0.3s ease-out;
                animation: slideIn 0.3s ease-out forwards;
            }
            .custom-toast.fade-out {
                transform: translateX(100%);
                opacity: 0;
            }
            .custom-toast-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 12px 16px;
                border-bottom: 1px solid #eee;
                background: #f8f9fa;
                border-radius: 8px 8px 0 0;
            }
            .custom-toast-close {
                background: none;
                border: none;
                font-size: 20px;
                cursor: pointer;
                line-height: 1;
                padding: 0;
            }
            .custom-toast-body {
                padding: 12px 16px;
                color: #333;
            }
            @keyframes slideIn {
                from { transform: translateX(100%); opacity: 0; }
                to { transform: translateX(0); opacity: 1; }
            }
        `;
        document.head.appendChild(style);
    });
}