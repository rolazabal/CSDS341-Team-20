import java.sql.*;
import java.util.Scanner;

public class GameDatabaseUI {
    // Fake PostgreSQL connection details
    private static final String DB_URL = "link";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Connected to the database successfully!");
            connection.setAutoCommit(false); // Enable manual transaction control
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("\nMain Menu:");
                System.out.println("1. Manage Players");
                System.out.println("2. Manage Characters");
                System.out.println("3. Manage Quests");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        managePlayers(connection, scanner);
                        break;
                    case 2:
                        manageCharacters(connection, scanner);
                        break;
                    case 3:
                        manageQuests(connection, scanner);
                        break;
                    case 4:
                        running = false;
                        System.out.println("Exiting the program. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    private static void managePlayers(Connection connection, Scanner scanner) {
        System.out.println("\nManage Players:");
        System.out.println("1. Add a Player");
        System.out.println("2. View All Players");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                addPlayer(connection, scanner);
                break;
            case 2:
                viewPlayers(connection);
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice. Returning to Main Menu.");
        }
    }

    private static void addPlayer(Connection connection, Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        String sql = "INSERT INTO player (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            connection.commit(); // Commit the transaction on success
            System.out.println("Player added successfully.");
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback the transaction on failure
                System.out.println("Transaction rolled back due to error: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                System.out.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
        }
    }

    private static void viewPlayers(Connection connection) {
        String sql = "SELECT playerID, username FROM player";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Player List:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("playerID") + ", Username: " + rs.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving players: " + e.getMessage());
        }
    }

    private static void manageCharacters(Connection connection, Scanner scanner) {
        System.out.println("\nManage Characters:");
        System.out.println("1. Add a Character");
        System.out.println("2. View Characters by Player ID");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                addCharacter(connection, scanner);
                break;
            case 2:
                viewCharactersForPlayer(connection, scanner);
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice. Returning to Main Menu.");
        }
    }

    private static void addCharacter(Connection connection, Scanner scanner) {
        System.out.print("Enter Player ID: ");
        int playerID = scanner.nextInt();
        System.out.print("Enter Character Name: ");
        String charName = scanner.next();
        System.out.print("Enter Class ID: ");
        int classID = scanner.nextInt();
        System.out.print("Enter XP: ");
        int xp = scanner.nextInt();

        String sql = "INSERT INTO character (playerID, charName, classID, xp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, playerID);
            pstmt.setString(2, charName);
            pstmt.setInt(3, classID);
            pstmt.setInt(4, xp);
            pstmt.executeUpdate();
            connection.commit(); // Commit the transaction on success
            System.out.println("Character added successfully.");
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback the transaction on failure
                System.out.println("Transaction rolled back due to error: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                System.out.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
        }
    }

    private static void viewCharactersForPlayer(Connection connection, Scanner scanner) {
        System.out.print("Enter Player ID: ");
        int playerID = scanner.nextInt();

        String sql = "SELECT charID, charName, xp FROM character WHERE playerID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, playerID);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Characters for Player ID " + playerID + ":");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("charID") + ", Name: " + rs.getString("charName") + ", XP: " + rs.getInt("xp"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving characters: " + e.getMessage());
        }
    }

    private static void manageQuests(Connection connection, Scanner scanner) {
        System.out.println("\nManage Quests:");
        System.out.println("1. View All Quests");
        System.out.println("2. View Quests Completed by a Character");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                viewAllQuests(connection);
                break;
            case 2:
                viewQuestsCompletedByCharacter(connection, scanner);
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice. Returning to Main Menu.");
        }
    }

    private static void viewAllQuests(Connection connection) {
        String sql = "SELECT questID, objective, xpReward FROM quest";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Quest List:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("questID") + ", Objective: " + rs.getString("objective") + ", XP Reward: " + rs.getInt("xpReward"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving quests: " + e.getMessage());
        }
    }

    private static void viewQuestsCompletedByCharacter(Connection connection, Scanner scanner) {
        System.out.print("Enter Character ID: ");
        int charID = scanner.nextInt();

        String sql = "SELECT quest.questID, quest.objective FROM hasCompleted " +
                "JOIN quest ON hasCompleted.questID = quest.questID " +
                "WHERE hasCompleted.charID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, charID);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Quests Completed by Character ID " + charID + ":");
            while (rs.next()) {
                System.out.println("Quest ID: " + rs.getInt("questID") + ", Objective: " + rs.getString("objective"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving completed quests: " + e.getMessage());
        }
    }
}
