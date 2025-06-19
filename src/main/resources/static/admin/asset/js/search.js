document.addEventListener("DOMContentLoaded", function() {
    const searchInput = document.querySelector('.search-input');
    const userList = document.querySelectorAll('.user-item');

    // Hàm lọc danh sách khi người dùng nhập vào ô tìm kiếm
    function filterList() {
        const searchText = searchInput.value.toLowerCase(); // Lấy từ khóa tìm kiếm và chuyển về chữ thường

        userList.forEach((userItem) => {
            const userName = userItem.querySelector('.body-title-2').textContent.toLowerCase(); // Lấy tên người dùng và chuyển về chữ thường

            // Kiểm tra nếu tên người dùng chứa từ khóa tìm kiếm
            if (userName.includes(searchText)) {
                userItem.style.display = ''; // Hiển thị mục nếu tìm thấy
            } else {
                userItem.style.display = 'none'; // Ẩn mục nếu không tìm thấy
            }
        });
    }

    // Lắng nghe sự kiện 'input' của ô tìm kiếm
    searchInput.addEventListener('input', filterList);
});
