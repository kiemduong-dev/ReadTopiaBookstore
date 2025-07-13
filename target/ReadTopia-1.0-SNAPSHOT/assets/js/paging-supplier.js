console.log("✅ JS loaded: paging-supplier.js");

const contextPath = window.location.pathname.split("/")[1];
const baseURL = window.location.origin + "/" + contextPath;

function pagingSupplierPage(page) {
    fetch(`${baseURL}/admin/supplier/list?action=paging&value=${page}`)
        .then(res => res.json())
        .then(data => {
            document.querySelector('.admin-main-content-body-tbody').innerHTML = data.rowsHtml;
            document.querySelector('.admin-main-content-pagination').innerHTML = data.paginationHtml;
        })
        .catch(err => console.error("❌ Pagination error:", err));
}

function searchSupplier() {
    const keyword = document.getElementById("searchSupplierInput").value.trim();
    console.log("🔍 Searching for:", keyword);

    fetch(`${baseURL}/admin/supplier/list?action=search&query=${encodeURIComponent(keyword)}`)
        .then(res => res.json())
        .then(data => {
            document.querySelector(".admin-main-content-body-tbody").innerHTML = data.rowsHtml;
            document.querySelector(".admin-main-content-pagination").innerHTML = data.paginationHtml || "";
        })
        .catch(err => console.error("❌ Search error:", err));
}

document.addEventListener("DOMContentLoaded", () => {
    const input = document.getElementById("searchSupplierInput");
    if (input) {
        input.addEventListener("keyup", searchSupplier);
    }

    document.addEventListener("click", (e) => {
        if (e.target.classList.contains("page-btn")) {
            const page = parseInt(e.target.dataset.page);
            if (!isNaN(page)) {
                pagingSupplierPage(page);
            }
        }
    });
});
