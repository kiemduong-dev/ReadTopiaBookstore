<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty requestScope.successMessage}">
    <div class="success-message">
        <strong>Success:</strong>
        <span>${requestScope.successMessage}</span>
    </div>
</c:if>
