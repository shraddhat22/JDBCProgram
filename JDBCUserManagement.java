import java.sql.*;
import java.util.Scanner;

public class JDBCUserManagement {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/JDBC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Create User");
                System.out.println("2. Read All Users");
                System.out.println("3. Read User by ID");
                System.out.println("4. Read Users by Name");
                System.out.println("5. Update User");
                System.out.println("6. Delete User");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        createUser(scanner);
                        break;
                    case 2:
                        readAllUsers();
                        break;
                    case 3:
                        readUserById(scanner);
                        break;
                    case 4:
                        readUsersByName(scanner);
                        break;
                    case 5:
                        updateUser(scanner);
                        break;
                    case 6:
                        deleteUser(scanner);
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    private static void createUser(Scanner scanner) {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("INSERT INTO Users (name, email) VALUES (?, ?)")) {
            System.out.print("Enter user name: ");
            String name = scanner.next();
            System.out.print("Enter user email: ");
            String email = scanner.next();
            statement.setString(1, name);
            statement.setString(2, email);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User created successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void readAllUsers() {
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Users")) {
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                        ", Name: " + resultSet.getString("name") +
                        ", Email: " + resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void readUserById(Scanner scanner) {
        System.out.print("Enter user ID: ");
        int id = scanner.nextInt();
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Users WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id") +
                            ", Name: " + resultSet.getString("name") +
                            ", Email: " + resultSet.getString("email"));
                } else {
                    System.out.println("No user found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void readUsersByName(Scanner scanner) {
        System.out.print("Enter user name: ");
        String name = scanner.next();
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Users WHERE name = ?")) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id") +
                            ", Name: " + resultSet.getString("name") +
                            ", Email: " + resultSet.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateUser(Scanner scanner) {
        System.out.print("Enter user ID to update: ");
        int id = scanner.nextInt();
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Users WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.print("Enter new name: ");
                    String newName = scanner.next();
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.next();
                    try (PreparedStatement updateStatement = conn.prepareStatement("UPDATE Users SET name = ?, email = ? WHERE id = ?")) {
                        updateStatement.setString(1, newName);
                        updateStatement.setString(2, newEmail);
                        updateStatement.setInt(3, id);
                        int rowsUpdated = updateStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("User updated successfully.");
                        }
                    }
                } else {
                    System.out.println("No user found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteUser(Scanner scanner) {
        System.out.print("Enter user ID to delete: ");
        int id = scanner.nextInt();
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("DELETE FROM Users WHERE id = ?")) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("No user found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
