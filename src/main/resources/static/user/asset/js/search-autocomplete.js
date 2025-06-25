let debounceTimer;

document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("search-input");
    const resultBox = document.querySelector(".search-results");
    const resultList = document.getElementById("search-suggestions");
    const searchTextSpan = document.querySelector(".search-text");
    const searchFail = document.querySelector(".search-fail");
    const searchMore = document.querySelector(".search-more");
    const seeAllLink = document.getElementById("see-all-results-link") || searchMore.querySelector("a");

    searchInput.addEventListener("input", function () {
        clearTimeout(debounceTimer);
        const keyword = this.value.trim();

        if (keyword.length < 2) {
            hideResultBox();
            return;
        }

        debounceTimer = setTimeout(() => {
            fetch(`/api/products/search?keyword=${encodeURIComponent(keyword)}`)
                .then(res => {
                    if (!res.ok) throw new Error("HTTP error " + res.status);
                    return res.json();
                })
                .then(products => {
                    resultList.innerHTML = "";
                    searchTextSpan.textContent = keyword;
                    seeAllLink.href = `/products?name=${encodeURIComponent(keyword)}`;

                    if (products.length === 0) {
                        searchFail.classList.remove("d-none");
                        searchMore.classList.add("d-none");
                        showResultBox();
                        return;
                    }

                    searchFail.classList.add("d-none");
                    searchMore.classList.remove("d-none");
                    seeAllLink.textContent = `See all ${products.length} results`;

                    products.forEach(p => {
                        const li = document.createElement("li");
                        li.style.display = "flex";
                        li.style.alignItems = "center";
                        li.style.gap = "10px";
                        li.style.marginBottom = "8px";

                        li.innerHTML = `
                            <a href="/products/${p.id}" style="display: flex; align-items: center; padding: 8px; gap: 10px;">
                                <img src="${p.thumbnail}" width="48" height="48" style="object-fit: cover; border-radius: 4px;">
                                <div style="flex: 1;">
                                    <div style="font-weight: 500;">${p.name}</div>
                                    <div style="color: #007bff; font-size: 14px;">
                                        ${p.price}đ
                                    </div>
                                </div>
                            </a>
                        `;
                        resultList.appendChild(li);
                    });

                    showResultBox();
                })

                .catch(err => {
                    hideResultBox();
                });
        }, 100);
    });

    document.addEventListener("click", function (e) {
        if (!e.target.closest(".search-bar")) {
            hideResultBox();
        }
    });

    function showResultBox() {
        resultBox.classList.add("active");
        resultBox.style.display = "block";
    }

    function hideResultBox() {
        resultBox.classList.remove("active");
        resultBox.style.display = "none";
    }
});
