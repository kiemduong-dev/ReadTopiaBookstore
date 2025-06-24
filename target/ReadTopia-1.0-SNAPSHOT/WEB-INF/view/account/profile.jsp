<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<jsp:include page="/WEB-INF/includes/head.jsp" />

<body>
    <!-- Header -->
    <jsp:include page="/WEB-INF/includes/header.jsp" />

    <div class="form-container">
        <h2>👤 Profile</h2>

        <!-- Toast message -->
        <jsp:include page="/WEB-INF/includes/toast.jsp" />

        <form method="post" action="${pageContext.request.contextPath}/edit-profile" id="profileForm">
            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">* Username</label>
                    <input type="text" id="profileUsername" class="form-input" name="username" value="${user.username}" readonly />
                </div>
                <div class="form-group">
                    <label class="form-label">* Date of Birth</label>
                    <input type="date" id="profileDob" class="form-input" name="dob" value="${user.dob}" readonly />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">* First Name</label>
                    <input type="text" id="profileFirstName" class="form-input" name="firstName" value="${user.firstName}" readonly />
                </div>
                <div class="form-group">
                    <label class="form-label">* Last Name</label>
                    <input type="text" id="profileLastName" class="form-input" name="lastName" value="${user.lastName}" readonly />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">* Email</label>
                    <input type="email" id="profileEmail" class="form-input" name="email" value="${user.email}" readonly />
                </div>
                <div class="form-group">
                    <label class="form-label">* Role</label>
                    <select id="profileRole" class="form-input" disabled>
                        <option ${user.role == 1 ? 'selected' : ''}>Customer</option>
                        <option ${user.role == 0 ? 'selected' : ''}>Staff</option>
                        <option ${user.role == 2 ? 'selected' : ''}>Admin</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">* Phone</label>
                    <input type="text" id="profilePhone" class="form-input" name="phone" value="${user.phone}" readonly />
                </div>
                <div class="form-group">
                    <label class="form-label">* Sex</label>
                    <div class="radio-group">
                        <div class="radio-item">
                            <input type="radio" name="sex" id="profileMale" value="0" ${user.sex == 0 ? 'checked' : ''} disabled /> Male
                        </div>
                        <div class="radio-item">
                            <input type="radio" name="sex" id="profileFemale" value="1" ${user.sex == 1 ? 'checked' : ''} disabled /> Female
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">* Address</label>
                <textarea name="address" id="profileAddress" class="form-input" rows="3" readonly>${user.address}</textarea>
            </div>

            <!-- Action Buttons -->
            <div class="btn-group">
                <button type="button" class="btn btn-secondary" id="editProfileBtn" onclick="toggleProfileEdit()">Edit</button>
                <a href="${pageContext.request.contextPath}/change-password" class="btn btn-primary">Change Password</a>
            </div>
        </form>
    </div>

    <!-- Footer -->
    <jsp:include page="/WEB-INF/includes/footer.jsp" />

    <!-- Inline JS (phụ thuộc vào JS bạn đã gửi) -->
    <script>
        function toggleProfileEdit() {
            const readonly = document.getElementById('profileDob').readOnly;
            const fields = ['profileDob', 'profileFirstName', 'profileLastName', 'profileEmail', 'profilePhone', 'profileAddress'];
            fields.forEach(id => {
                document.getElementById(id).readOnly = !readonly;
            });
            document.getElementById('profileMale').disabled = !document.getElementById('profileMale').disabled;
            document.getElementById('profileFemale').disabled = !document.getElementById('profileFemale').disabled;

            const btn = document.getElementById('editProfileBtn');
            if (readonly) {
                btn.textContent = 'Save';
                btn.className = 'btn btn-primary';
                btn.type = 'submit'; // sau khi bấm Edit → chuyển sang submit
            } else {
                btn.textContent = 'Edit';
                btn.className = 'btn btn-secondary';
                btn.type = 'button';
                showSuccessMessage('Profile updated successfully');
            }
        }

        function showSuccessMessage(message) {
            const existing = document.querySelector('.success-message');
            if (existing) existing.remove();

            const msg = document.createElement('div');
            msg.className = 'success-message';
            msg.innerHTML = `<i class="fas fa-check-circle"></i><span>${message}</span>`;
            document.body.insertBefore(msg, document.body.firstChild);
            setTimeout(() => msg.remove(), 3000);
        }
    </script>
</body>
</html>
