// 1. Load books khi trang được tải
function loadBooks() {
    // Sẽ render từ server bằng JSP hoặc Controller
}

// 2. Hiển thị chi tiết sách
function showBookDetail(bookId) {
    const bookCard = document.querySelector(`.book-card[onclick="showBookDetail(${bookId})"]`);
    if (bookCard) {
        document.getElementById('bookDetailTitle').textContent = bookCard.querySelector('.book-title').textContent;
        document.getElementById('bookDetailPrice').textContent = bookCard.querySelector('.book-price').textContent;
        document.getElementById('bookDetailImage').src = bookCard.querySelector('img.book-image').src;
        document.getElementById('bookDetailModal').style.display = 'block';
    }
}

// 3. Dropdown và Auth
function toggleDropdown() {
    document.getElementById('userDropdown').classList.toggle('show');
}
window.onclick = function (e) {
    if (!e.target.closest('.user-menu')) {
        document.getElementById('userDropdown').classList.remove('show');
    }
};

function showLoginModal() {
    document.getElementById('loginModal').style.display = 'block';
    toggleDropdown();
}
function showRegisterModal() {
    closeModal('loginModal');
    document.getElementById('registerModal').style.display = 'block';
}
function closeModal(id) {
    document.getElementById(id).style.display = 'none';
}

// 4. Đăng nhập/đăng ký đơn giản
let currentUser = null;
document.getElementById('loginForm').onsubmit = function (e) {
    e.preventDefault();
    const username = new FormData(e.target).get('username');
    currentUser = {username};
    updateUIForLoggedInUser(username);
    closeModal('loginModal');
};

document.getElementById('registerForm').onsubmit = function (e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    if (formData.get('password') !== formData.get('confirmPassword')) {
        alert('Passwords do not match!');
        return;
    }
    closeModal('registerModal');
    showSuccessMessage('Account registered');
};

// 5. UI sau khi đăng nhập
function updateUIForLoggedInUser(username) {
    document.getElementById('username').textContent = username;
    document.getElementById('loginLink').style.display = 'none';
    document.getElementById('logoutLink').style.display = 'block';
}

function logout() {
    currentUser = null;
    document.getElementById('username').textContent = 'Login';
    document.getElementById('loginLink').style.display = 'block';
    document.getElementById('logoutLink').style.display = 'none';
    showSuccessMessage('Logged out');
}

// 6. Thêm vào giỏ
function addToCart(bookId) {
    if (!currentUser) {
        showLoginModal();
        return;
    }
    showSuccessMessage('Added to cart (demo)');
}

// 7. Hiển thị thông báo đơn giản
function showSuccessMessage(msg) {
    const div = document.createElement('div');
    div.className = 'success-message';
    div.innerHTML = `<i class="fas fa-check-circle"></i> <span>${msg}</span>`;
    document.body.appendChild(div);
    setTimeout(() => div.remove(), 2000);
}

// 8. Tìm kiếm
const searchInput = document.querySelector('.search-input');
if (searchInput) {
    searchInput.addEventListener('input', function (e) {
        const keyword = e.target.value.toLowerCase();
        document.querySelectorAll('.book-card').forEach(card => {
            const title = card.querySelector('.book-title').textContent.toLowerCase();
            card.style.display = title.includes(keyword) ? '' : 'none';
        });
    });
}


// 9. Sắp xếp
document.querySelector('.filter-select').addEventListener('change', function (e) {
    const sortBy = e.target.value;
    const grid = document.getElementById('booksGrid');
    const cards = Array.from(grid.children);

    cards.sort((a, b) => {
        if (sortBy === 'price-asc' || sortBy === 'price-desc') {
            const priceA = parseFloat(a.querySelector('.book-price').textContent.replace(/[^\d]/g, ''));
            const priceB = parseFloat(b.querySelector('.book-price').textContent.replace(/[^\d]/g, ''));
            return sortBy === 'price-asc' ? priceA - priceB : priceB - priceA;
        } else if (sortBy === 'name-asc' || sortBy === 'name-desc') {
            const nameA = a.querySelector('.book-title').textContent.toLowerCase();
            const nameB = b.querySelector('.book-title').textContent.toLowerCase();
            return sortBy === 'name-asc' ? nameA.localeCompare(nameB) : nameB.localeCompare(nameA);
        }
        return 0; // default no sort
    });

    grid.innerHTML = '';
    cards.forEach(card => grid.appendChild(card));
});


// 10. Khởi tạo
document.addEventListener('DOMContentLoaded', function () {
    loadBooks();
});
