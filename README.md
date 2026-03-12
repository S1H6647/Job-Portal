# Job Portal API

This is a Spring Boot 4.0.3 (Java 21) based backend for a Job Portal system.

## 🚀 API Documentation (OpenAPI / Swagger)

The project uses **SpringDoc OpenAPI** to automatically generate documentation for all REST endpoints. You can explore and test the API directly from your browser.

### **How to access the documentation:**

Once the application is running, open your browser and navigate to:

👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

### **Key Features of the API Docs:**

*   **Interactive UI:** Test endpoints directly without needing Postman.
*   **Authentication:** Click the **"Authorize"** button and paste your JWT token (format: `Bearer <your_token>`) to access protected routes.
*   **Models:** View the request and response schemas for all entities.
*   **Raw OpenAPI Spec:** Access the raw JSON definition at [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).

---

## 🛠 Tech Stack

*   **Framework:** Spring Boot 4.0.3
*   **Java Version:** 21
*   **Security:** Spring Security + JWT
*   **Database:** PostgreSQL
*   **Documentation:** SpringDoc OpenAPI / Swagger UI
*   **File Storage:** Cloudinary
*   **Email:** Gmail SMTP
