<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty requestScope.successMessage}">
    <div class="toast toast-success" role="alert" aria-live="polite" aria-atomic="true">
        <strong>Success:</strong>
        <span>${requestScope.successMessage}</span>
    </div>
</c:if>
