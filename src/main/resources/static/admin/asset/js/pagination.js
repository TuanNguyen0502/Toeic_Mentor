let thisPage = 1; // Trang hiện tại
let limit = 10;   // Số mục hiển thị mỗi trang
let originalList = [];  // Danh sách gốc ban đầu (node)
let filteredList = [];  // Danh sách đã lọc (dùng cho phân trang)

document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.querySelector('.search-input');
    originalList = Array.from(document.querySelectorAll('.list .user-item'));
    filteredList = [...originalList]; // Ban đầu, danh sách đã lọc là toàn bộ

    searchInput.addEventListener('input', filterList);
    loadItem();
});

function filterList() {
    const searchText = document.querySelector('.search-input').value.toLowerCase();

    filteredList = originalList.filter((userItem) => {
        const userName = userItem.querySelector('.body-title-2').textContent.toLowerCase();
        return userName.includes(searchText);
    });

    thisPage = 1; // Reset về trang đầu tiên sau khi lọc
    loadItem();
}

// Hàm tải mục người dùng cho mỗi trang
function loadItem() {
    let beginGet = limit * (thisPage - 1);
    let endGet = limit * thisPage - 1;

    // Lặp qua danh sách người dùng và ẩn/hiện tùy thuộc vào trang
    originalList.forEach(item => item.style.display = 'none'); // Ẩn hết

    filteredList.forEach((item, key) => {
        if (key >= beginGet && key <= endGet) {
            item.style.display = 'flex'; // Hiển thị các phần tử phù hợp trang
        }
    });

    // Cập nhật phân trang
    listPage(filteredList.length);
}

// Hàm cập nhật phân trang
function listPage(totalItems) {
    const count = Math.ceil(totalItems / limit);
    const paginationContainer = document.querySelector('.listPage');

    // Xóa nội dung cũ trong phân trang
    paginationContainer.innerHTML = '';

    if (count === 0) return; // Không hiển thị phân trang nếu không có item

    // Nút Previous: chỉ hiển thị khi không phải trang đầu tiên
    if (thisPage > 1) {
        const prev = document.createElement('li');
        prev.innerHTML = '<a href="#"><i class="icon-chevron-left"></i></a>';
        prev.onclick = () => changePage(thisPage - 1);
        paginationContainer.appendChild(prev);
    }

    // Các số trang: tạo một nút cho mỗi trang
    for (let i = 1; i <= count; i++) {
        const pageItem = document.createElement('li');
        if (i === thisPage) pageItem.classList.add('active');
        pageItem.innerHTML = `<a href="#">${i}</a>`;
        pageItem.onclick = () => changePage(i);
        paginationContainer.appendChild(pageItem);
    }

    // Nút Next: chỉ hiển thị khi không phải trang cuối cùng
    if (thisPage < count) {
        const next = document.createElement('li');
        next.innerHTML = '<a href="#"><i class="icon-chevron-right"></i></a>';
        next.onclick = () => changePage(thisPage + 1);
        paginationContainer.appendChild(next);
    }
}

// Hàm thay đổi trang
function changePage(page) {
    thisPage = page; // Cập nhật trang hiện tại
    loadItem(); // Tải lại các mục người dùng
}
