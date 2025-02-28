import java.sql.*;

public class VulnerableStoredProcedureExample {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/your_database";
        String user = "your_username";
        String password = "your_password";

        // Simulated user input (can be manipulated by an attacker)
        String employeeId = "101); DROP TABLE employees; --";

        callStoredProcedure(url, user, password, employeeId);
    }

    public static void callStoredProcedure(String url, String user, String password, String employeeId) {
        // ❌ VULNERABLE: Directly concatenating user input into SQL query
        String procedureCall = "{CALL getEmployeeName(" + employeeId + ")}";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {  // ❌ Using Statement instead of PreparedStatement

            // ❌ Executing raw SQL, allowing SQL injection
            ResultSet rs = stmt.executeQuery(procedureCall);

            while (rs.next()) {
                System.out.println("Employee Name: " + rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
