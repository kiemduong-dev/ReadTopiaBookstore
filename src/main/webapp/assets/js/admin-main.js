// Page navigation
function showPage(pageId, event) {
    // Hide all pages
    const pages = document.querySelectorAll('.page');
    pages.forEach(page => page.classList.remove('active'));

    // Show selected page
    const targetPage = document.getElementById(pageId);
    if (targetPage) {
        targetPage.classList.add('active');
    }

    // Update active menu item
    if (event && event.target) {
        const menuItems = document.querySelectorAll('.menu-item');
        menuItems.forEach(item => item.classList.remove('active'));
        event.target.classList.add('active');
    }
}

// Initialize revenue chart
function initRevenueChart() {
    const ctx = document.getElementById('revenueChart');
    if (!ctx) return;

    new Chart(ctx.getContext('2d'), {
        type: 'line',
        data: {
            labels: [], // dữ liệu thời gian sẽ được load từ server
            datasets: [
                // dữ liệu import, export, revenue sẽ được fetch và đưa vào ở backend hoặc API
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top',
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function (value) {
                            return value.toLocaleString() + ' VNĐ';
                        }
                    }
                }
            }
        }
    });
}

// Modal functions - chỉ mở modal, không gán dữ liệu mẫu
function showAddAccountModal() {
    const modal = document.getElementById('addAccountModal');
    if (modal) modal.style.display = 'block';
}

function viewAccount(username) {
    // TODO: load dữ liệu account từ backend và gán vào modal trước khi show
    const modal = document.getElementById('viewAccountModal');
    if (modal) modal.style.display = 'block';
}

function editAccount(username) {
    // TODO: load dữ liệu account từ backend và gán vào modal trước khi show
    const modal = document.getElementById('editAccountModal');
    if (modal) modal.style.display = 'block';
}

let deleteAccountUsername = '';
function deleteAccount(username) {
    deleteAccountUsername = username;
    const deleteMsg = document.getElementById('deleteMessage');
    if (deleteMsg) deleteMsg.textContent = `Do you want to delete ${username} account?`;

    const modal = document.getElementById('deleteConfirmModal');
    if (modal) modal.style.display = 'block';
}

function confirmDelete() {
    // TODO: gọi API backend xóa account, ví dụ fetch hoặc AJAX

    showSuccessMessage(`Account ${deleteAccountUsername} has been deleted successfully`);
    closeModal('deleteConfirmModal');
    deleteAccountUsername = '';
}

function showChangePasswordModal() {
    closeModal('profileModal');
    const modal = document.getElementById('changePasswordModal');
    if (modal) modal.style.display = 'block';
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) modal.style.display = 'none';
}

// Form submissions (giữ nguyên, xử lý gửi form thực tế ở backend)
const addForm = document.getElementById('addAccountForm');
if (addForm) {
    addForm.onsubmit = function (e) {
        e.preventDefault();
        // TODO: gửi dữ liệu lên backend
        showSuccessMessage('Account has been added successfully');
        closeModal('addAccountModal');
        this.reset();
    };
}

const editForm = document.getElementById('editAccountForm');
if (editForm) {
    editForm.onsubmit = function (e) {
        e.preventDefault();
        // TODO: gửi dữ liệu cập nhật lên backend
        showSuccessMessage('Account has been updated successfully');
        closeModal('editAccountModal');
    };
}

const changePwdForm = document.getElementById('changePasswordForm');
if (changePwdForm) {
    changePwdForm.onsubmit = function (e) {
        e.preventDefault();
        const formData = new FormData(e.target);
        const newPassword = formData.get('newPassword');
        const confirmPassword = formData.get('confirmPassword');

        if (newPassword !== confirmPassword) {
            alert('New password and confirm password do not match!');
            return;
        }

        // TODO: gửi đổi mật khẩu lên backend
        showSuccessMessage('Password has been changed successfully');
        closeModal('changePasswordModal');
        this.reset();
    };
}

// Success message function
function showSuccessMessage(message) {
    const existing = document.querySelector('.success-message');
    if (existing) {
        existing.remove();
    }

    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.innerHTML = `
        <i class="fas fa-check-circle"></i>
        <span>${message}</span>
    `;

    document.body.insertBefore(successDiv, document.body.firstChild);

    setTimeout(() => {
        successDiv.remove();
    }, 3000);
}

// Search functionality for account
const accountSearchInput = document.getElementById('accountSearch');
if (accountSearchInput) {
    accountSearchInput.addEventListener('input', function (e) {
        const searchTerm = e.target.value.toLowerCase();
        const rows = document.querySelectorAll('#accountTableBody tr');

        rows.forEach(row => {
            const username = row.cells[0].textContent.toLowerCase();
            const fullName = row.cells[1].textContent.toLowerCase();

            if (username.includes(searchTerm) || fullName.includes(searchTerm)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });
}

// Initialize charts (bạn gọi sau khi trang load)
document.addEventListener('DOMContentLoaded', function () {
    initRevenueChart();
    // Bạn cũng có thể thêm initIncomeChart() tương tự khi có dữ liệu
});

// Close modals when clicking outside
window.onclick = function (event) {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
};
