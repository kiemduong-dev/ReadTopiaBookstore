<%-- 
    File        : footer.jsp
    Description : Footer for ReadTopia bookstore system
    Author      : CE181518 Duong An Kiem
--%>

<footer class="footer text-white mt-5 pt-5 pb-4" style="background-color: #2196f3;">
    <div class="container">
        <div class="row align-items-center mb-4">

            <!-- Logo + Slogan -->
            <div class="col-md-4 mb-4">
                <a href="${pageContext.request.contextPath}/homepage/book/list" class="d-flex align-items-center text-white text-decoration-none mb-2">
                    <img src="${pageContext.request.contextPath}/assets/img/logo.png" alt="ReadTopia Logo" style="height: 50px; margin-right: 10px;">
                    <span style="font-size: 20px; font-weight: bold;">ReadTopia</span>
                </a>
                <p class="small">
                    ReadTopia is an online bookstore. We sell good books at low prices. Fast delivery and friendly support.
                </p>
            </div>

            <!-- Useful Links -->
            <div class="col-md-2 mb-4">
                <h6 class="fw-bold text-uppercase mb-3">Links</h6>
                <ul class="list-unstyled small">
                    <li><a href="${pageContext.request.contextPath}/homepage/book/list" class="text-white text-decoration-none">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/homepage/book/list" class="text-white text-decoration-none">Books</a></li>
                    <li><a href="#" class="text-white text-decoration-none">About Us</a></li>
                    <li><a href="#" class="text-white text-decoration-none">Contact</a></li>
                    <li><a href="#" class="text-white text-decoration-none">Privacy Policy</a></li>
                    <li><a href="#" class="text-white text-decoration-none">Terms of Use</a></li>
                </ul>
            </div>

            <!-- Contact Info -->
            <div class="col-md-3 mb-4">
                <h6 class="fw-bold text-uppercase mb-3">Contact</h6>
                <ul class="list-unstyled small">
                    <li><i class="bi bi-geo-alt me-2"></i>FPT University, Can Tho City</li>
                    <li><i class="bi bi-telephone me-2"></i>0343 634 698</li>
                    <li><i class="bi bi-envelope me-2"></i>support@readtopia.vn</li>
                </ul>
            </div>

            <!-- Social -->
            <div class="col-md-3 mb-4">
                <h6 class="fw-bold text-uppercase mb-3">Follow us</h6>
                <div class="d-flex gap-3 fs-5">
                    <a href="#" class="text-white"><i class="bi bi-facebook"></i></a>
                    <a href="#" class="text-white"><i class="bi bi-instagram"></i></a>
                    <a href="#" class="text-white"><i class="bi bi-youtube"></i></a>
                    <a href="#" class="text-white"><i class="bi bi-twitter"></i></a>
                </div>
            </div>

        </div>

        <hr class="border-white" />

        <!-- Bottom Footer -->
        <div class="row small">
            <div class="col-md-6">
                <p class="mb-0">&copy; 2025 ReadTopia. All rights reserved.</p>
            </div>
            <div class="col-md-6 text-end">
                <p class="mb-0">Built with ?? by <a href="#" class="text-white text-decoration-underline">ReadTopia Dev Team</a></p>
            </div>
        </div>
    </div>
</footer>
