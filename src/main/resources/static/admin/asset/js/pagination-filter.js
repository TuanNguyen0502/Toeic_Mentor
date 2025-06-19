function initPaginationAndFilter({
                                     itemSelector,
                                     listContainerSelector,
                                     searchInputSelector,
                                     nameSelector,
                                     paginationContainerSelector,
                                     limitSelectSelector, // THÊM
                                     displayStyle = 'flex',
                                     itemsPerPage = 10
                                 }) {
    let thisPage = 1;
    let limit = itemsPerPage;
    let originalList = [];
    let filteredList = [];

    const searchInput = document.querySelector(searchInputSelector);
    const limitSelect = document.querySelector(limitSelectSelector);
    originalList = Array.from(document.querySelectorAll(`${listContainerSelector} ${itemSelector}`));
    filteredList = [...originalList];

    function filterList() {
        const searchText = searchInput.value.toLowerCase();
        filteredList = originalList.filter((item) => {
            const itemName = item.querySelector(nameSelector).textContent.toLowerCase();
            return itemName.includes(searchText);
        });
        thisPage = 1;
        loadItem();
    }

    function loadItem() {
        let beginGet = limit * (thisPage - 1);
        let endGet = limit * thisPage - 1;
        originalList.forEach(item => item.style.display = 'none');
        filteredList.forEach((item, key) => {
            if (key >= beginGet && key <= endGet) {
                item.style.display = displayStyle;
            }
        });
        listPage(filteredList.length);
    }

    function listPage(totalItems) {
        const count = Math.ceil(totalItems / limit);
        const paginationContainer = document.querySelector(paginationContainerSelector);
        paginationContainer.innerHTML = '';

        if (count === 0) return;

        if (thisPage > 1) {
            const prev = document.createElement('li');
            prev.innerHTML = '<a href="#"><i class="icon-chevron-left"></i></a>';
            prev.onclick = () => changePage(thisPage - 1);
            paginationContainer.appendChild(prev);
        }

        for (let i = 1; i <= count; i++) {
            const pageItem = document.createElement('li');
            if (i === thisPage) pageItem.classList.add('active');
            pageItem.innerHTML = `<a href="#">${i}</a>`;
            pageItem.onclick = () => changePage(i);
            paginationContainer.appendChild(pageItem);
        }

        if (thisPage < count) {
            const next = document.createElement('li');
            next.innerHTML = '<a href="#"><i class="icon-chevron-right"></i></a>';
            next.onclick = () => changePage(thisPage + 1);
            paginationContainer.appendChild(next);
        }
    }

    function changePage(page) {
        thisPage = page;
        loadItem();
    }

    if (limitSelect) {
        limitSelect.addEventListener('change', function () {
            limit = parseInt(this.value);
            thisPage = 1;
            loadItem();
        });
    }

    searchInput.addEventListener('input', filterList);
    loadItem();
}
