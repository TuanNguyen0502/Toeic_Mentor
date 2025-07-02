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

    // Load notifications on dropdown open
    const notificationList = document.getElementById('notificationList');
    const dropdownButton = document.getElementById('dropdownMenuButton1');
    if (dropdownButton && notificationList) {
        dropdownButton.addEventListener('click', function() {
            fetch('/admin/notifications')
                .then(response => response.json())
                .then(notifications => {
                    // Remove old notification items (except header and view all)
                    const items = notificationList.querySelectorAll('li');
                    items.forEach((li, idx) => {
                        if (idx !== 0 && idx !== items.length - 1) li.remove();
                    });
                    // Insert new notifications
                    const viewAllLi = notificationList.lastElementChild;
                    notifications.forEach(notification => {
                        const li = document.createElement('li');
                        li.innerHTML = `
                            <div class="noti-item w-full wg-user active">
                                <div class="flex-grow">
                                    <div class="flex items-center justify-between">
                                        <a href="/admin/notifications/${notification.id}" class="body-title">${notification.title}</a>
                                        <div class="time">${notification.createdAt || ''}</div>
                                    </div>
                                </div>
                            </div>
                        `;
                        notificationList.insertBefore(li, viewAllLi);
                    });
                })
                .catch(error => {
                    console.error('Error fetching notifications:', error);
                });
        });
    }
});