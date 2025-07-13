console.log("✅ JS loaded: paging-stock.js");

// 👉 Call this when user clicks a page number or next/prev
function pagingStock(page) {
    if (page < 1) return;

    fetch(`/ReadTopia/admin/stock/list?action=paging&value=${page}`)
        .then(res => res.json())
        .then(data => {
            // ✅ Update table rows
            document.querySelector(".stock-body").innerHTML = data.rowsHtml;

            // ✅ Update pagination bar
            document.querySelector(".pagination").innerHTML = data.paginationHtml;
        })
        .catch(err => console.error("❌ Paging failed:", err));
}

// 👉 Optional if you want to manually update active state
function updateStockPagination(activePage) {
    document.querySelectorAll(".page-btn").forEach(btn => {
        const pageNum = parseInt(btn.dataset.page);
        btn.classList.remove("active");
        if (pageNum === activePage) btn.classList.add("active");
    });
}  
