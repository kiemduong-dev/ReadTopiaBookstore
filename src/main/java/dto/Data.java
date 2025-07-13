package dto;

import java.util.List;

/**
 * Utility class for generating HTML table rows and pagination for AJAX.
 */
public class Data {

    public String getInterfaceSupplier(List<SupplierDTO> list) {
        StringBuilder sb = new StringBuilder();
        for (SupplierDTO s : list) {
            sb.append("<tr>")
              .append("<td>").append(s.getSupID()).append("</td>")
              .append("<td>").append(escapeHtml(s.getSupName())).append("</td>")
              .append("<td>").append(escapeHtml(s.getSupPhone())).append("</td>")
              .append("<td>").append(escapeHtml(s.getSupAddress())).append("</td>")
              .append("<td><span class='status-badge ")
              .append(s.getSupStatus() == 1 ? "active'>Active" : "inactive'>Inactive")
              .append("</span></td>")
              .append("<td>")
              .append("<a href='detail?id=").append(s.getSupID()).append("' class='btn-icon btn-info'><i class='fas fa-eye'></i></a> ")
              .append("<a href='edit?id=").append(s.getSupID()).append("' class='btn-icon btn-warning'><i class='fas fa-edit'></i></a> ")
              .append("<a href='delete?id=").append(s.getSupID()).append("' class='btn-icon btn-danger' onclick=\"return confirm('Are you sure?')\"><i class='fas fa-trash'></i></a>")
              .append("</td></tr>");
        }
        return sb.toString().replace("\n", "").replace("\r", "");
    }

    public String getSupplierPaginationHTML(int currentPage, int totalPages) {
        StringBuilder sb = new StringBuilder("<ul class='pagination'>");
        boolean isFirstPage = currentPage <= 1;
        sb.append("<li><button class='page-btn' data-page='").append(currentPage - 1).append("' ")
          .append(isFirstPage ? "disabled" : "").append(">&lt;</button></li>");
        for (int i = 1; i <= totalPages; i++) {
            sb.append("<li><button class='page-btn ").append(i == currentPage ? "active" : "").append("' data-page='").append(i).append("'>")
              .append(i).append("</button></li>");
        }
        boolean isLastPage = currentPage >= totalPages;
        sb.append("<li><button class='page-btn' data-page='").append(currentPage + 1).append("' ")
          .append(isLastPage ? "disabled" : "").append(">&gt;</button></li>");
        sb.append("</ul>");
        return sb.toString().replace("\n", "").replace("\r", "");
    }


    public String getInterfaceImportStock(List<ImportStockDTO> list) {
        StringBuilder sb = new StringBuilder();
        for (ImportStockDTO s : list) {
            sb.append("<tr>")
              .append("<td>").append(s.getId()).append("</td>")
              .append("<td>").append(escapeHtml(s.getSupplierName())).append("</td>")
              .append("<td>").append(s.getImportDate()).append("</td>")
              .append("<td>").append(escapeHtml(s.getStaffName())).append("</td>")
              .append("<td>").append(String.format("%,.0f VND", s.getTotalPrice())).append("</td>")
              .append("<td><span class='status-badge ")
              .append(s.getStatus() == 1 ? "active" : "inactive").append("'>")
              .append(s.getStatus() == 1 ? "Active" : "Inactive").append("</span></td>")
              .append("<td><a href='ImportStockDetailServlet?isid=").append(s.getId())
              .append("' class='btn-icon btn-info'><i class='fas fa-eye'></i></a></td>")
              .append("</tr>");
        }
        return sb.toString().replace("\n", "").replace("\r", "");
    }

    public String getStockPaginationHTML(int currentPage, int totalPages) {
        StringBuilder sb = new StringBuilder("<ul class='pagination'>");
        boolean isFirstPage = currentPage <= 1;
        sb.append("<li><button class='page-btn' onclick='pagingStock(").append(currentPage - 1).append(")' ")
          .append("data-page='").append(currentPage - 1).append("' ")
          .append(isFirstPage ? "disabled" : "").append(">&lt;</button></li>");
        for (int i = 1; i <= totalPages; i++) {
            sb.append("<li><button class='page-btn ").append(i == currentPage ? "active" : "").append("' ")
              .append("onclick='pagingStock(").append(i).append(")' data-page='").append(i).append("'>")
              .append(i).append("</button></li>");
        }
        boolean isLastPage = currentPage >= totalPages;
        sb.append("<li><button class='page-btn' onclick='pagingStock(").append(currentPage + 1).append(")' ")
          .append("data-page='").append(currentPage + 1).append("' ")
          .append(isLastPage ? "disabled" : "").append(">&gt;</button></li>");
        sb.append("</ul>");
        return sb.toString().replace("\n", "").replace("\r", "");
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }
}
