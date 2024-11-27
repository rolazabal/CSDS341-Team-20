import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner; 

public class GameDatabaseUI {
    static String connectionUrl = 
            "jdbc:sqlserver://cxp-sql-03\\mls384;"
            + "database=DBQuest;"
            + "user=proj;"
            + "password=ProjPass1234!;"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "loginTimeout=15;";

    private static String username = null;
    private static boolean loggedIn = false;

    public static void main(String[] args) {
         try (Connection connection = DriverManager.getConnection(connectionUrl);)
        {
            System.out.println("Connected to the database successfully!");
            connection.setAutoCommit(false); // Enable manual transaction control
            Scanner scanner = new Scanner(System.in);
            boolean running = true;
            int playerID = -1;

            while (running) {
                while(!loggedIn && running) {
                    System.out.println("\nMain Menu:");
                    System.out.println("1. Login");
                    System.out.println("2. Sign Up");
                    System.out.println("3. Exit");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1:
                            playerID = login(connection, scanner);
                            break;
                        case 2:
                            signup(connection, scanner);
                            break;
                        case 3:
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }
                }
                while (loggedIn && running) {
                    System.out.println("\nWelcome Back "+ username);
                    System.out.println("\nMain Menu:");
                    System.out.println("1. Logout");
                    System.out.println("2. View my characters");
                    System.out.println("3. Manage my characters");
                    System.out.println("4. Create a new character");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();
    
                    switch (choice) {
                        case 1:
                            loggedIn = false;
                            break;
                        case 2:
                            fetchCharacters(connection);
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                        case 3:
                            manageCharacters(connection, scanner);
                            break;
                        case 4:
                            newCharacter(connection, scanner, playerID);
                            break;
    
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }

    }

    public static void newCharacter(Connection connection, Scanner scanner, int playerID) {
        boolean localRunning = true;
    
        while (localRunning) {
            System.out.println("\nInsert Character Name:");
            String name = scanner.nextLine();
    
            System.out.println("\nSelect Class:");
            String classView = "SELECT classID, name, health, stamina, speed FROM class";
    
            try (PreparedStatement choiceCall = connection.prepareStatement(classView)) {
                ResultSet resultSet = choiceCall.executeQuery();
    
                System.out.printf("%-5s %-20s %-10s %-10s %-10s%n", "ID", "Class Name", "Health", "Stamina", "Speed");
                System.out.println("---------------------------------------------------------------");
    
                while (resultSet.next()) {
                    int classID = resultSet.getInt("classID");
                    String className = resultSet.getString("name");
                    int health = resultSet.getInt("health");
                    int stamina = resultSet.getInt("stamina");
                    int speed = resultSet.getInt("speed");
    
                    System.out.printf("%-5d %-20s %-10d %-10d %-10d%n", classID, className, health, stamina, speed);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
            System.out.print("\nEnter the class ID of your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            if (name.length() > 4) {
                String sql = "{CALL newCharacter(?, ?, ?, ?)}";
    
                try (CallableStatement callableStatement = connection.prepareCall(sql)) {
                    connection.setAutoCommit(false); // Start transaction
    
                    callableStatement.setInt(1, playerID);
                    callableStatement.setInt(2, choice);
                    callableStatement.setString(3, name);
                    callableStatement.registerOutParameter(4, Types.INTEGER);
    
                    callableStatement.execute();
    
                    int newCharacterID = callableStatement.getInt(4);
                    System.out.println("New character created with ID: " + newCharacterID);
    
                    connection.commit(); // Commit transaction
                    localRunning = false;
    
                } catch (SQLException e) {
                    try {
                        connection.rollback(); // Roll back transaction
                        System.out.println("Transaction rolled back.");
                    } catch (SQLException rollbackEx) {
                        rollbackEx.printStackTrace();
                    }
                    e.printStackTrace();
                } finally {
                    try {
                        connection.setAutoCommit(true); // Reset auto-commit
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                System.out.println("Please enter a name that is at least 5 characters long.");
            }
        }
    }
    
    public static void signup(Connection connection, Scanner scanner) {
        boolean localrunning = true;
    
        while (localrunning) {
            System.out.println("Enter your username: ");
            String inputUsername = scanner.nextLine();
    
            System.out.println("Enter your password: ");
            String inputPassword = scanner.nextLine();
    
            if (inputPassword.length() < 4 || inputUsername.length() < 4) {
                System.out.println("Please enter a password and username of at least 4 characters.");
            } else {
                String sql = "SELECT * FROM player WHERE username = ?";
    
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, inputUsername);
    
                    ResultSet resultSet = preparedStatement.executeQuery();
    
                    if (!resultSet.isBeforeFirst()) { // Username not found
                        String signupCall = "{CALL newPlayer(?, ?)}";
                        try (CallableStatement signupCallableStatement = connection.prepareCall(signupCall)) {
                            connection.setAutoCommit(false); // Start transaction
    
                            signupCallableStatement.setString(1, inputUsername);
                            signupCallableStatement.setString(2, inputPassword);
                            signupCallableStatement.execute();
    
                            connection.commit(); // Commit transaction
                            localrunning = false;
                            System.out.println("Success! Please log in.");
                        } catch (SQLException e) {
                            try {
                                connection.rollback(); // Roll back transaction
                                System.out.println("Transaction rolled back.");
                            } catch (SQLException rollbackEx) {
                                rollbackEx.printStackTrace();
                            }
                            e.printStackTrace();
                        } finally {
                            try {
                                connection.setAutoCommit(true); // Reset auto-commit
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else {
                        System.out.println("That username is taken. Please try a different one.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    

    public static int login(Connection connection, Scanner scanner) {

        System.out.println("Enter your username: ");
        String inputUsername = scanner.nextLine();

        System.out.println("Enter your password: ");
        String inputPassword = scanner.nextLine();

        try {
            // Prepare the callable statement for the stored procedure
            String sql = "{CALL validateLogin(?, ?)}";
            CallableStatement callableStatement = connection.prepareCall(sql);

            // Set parameters
            callableStatement.setString(1, inputUsername);
            callableStatement.setString(2, inputPassword);

            // Execute the stored procedure
            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                int playerID = resultSet.getInt("playerID");
                String resultMessage = resultSet.getString("resultMessage");

                if (playerID != -1) {
                    username = inputUsername;
                    loggedIn = true;
                    System.out.println(resultMessage);
                } else {
                    System.out.println(resultMessage);
                }
                return playerID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void fetchCharacters(Connection connection) {
        System.out.println("fetching Characters");
        String sql = "{CALL fetchCharacters(?)}"; 

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.setString(1, username);

            System.out.println("Querying...");
            ResultSet resultSet = callableStatement.executeQuery();
            System.out.println("Query Done.");

            System.out.printf("%-15s %-10s %-15s %-10s %-10s %-10s%n",
                    "Character Name", "XP", "Class", "Health", "Speed", "Stamina");
            System.out.println("--------------------------------------------------------------");

            while (resultSet.next()) {
                String charName = resultSet.getString("charName");
                int xp = resultSet.getInt("xp");
                String className = resultSet.getString("name");
                int health = resultSet.getInt("health");
                int speed = resultSet.getInt("speed");
                int stamina = resultSet.getInt("stamina");

                // Print each character's details (excluding charID)
                System.out.printf("%-15s %-10d %-15s %-10d %-10d %-10d%n",
                        charName, xp, className, health, speed, stamina);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Print error details
        }
    }

    public static ArrayList<Integer> manageCharacters(Connection connection, Scanner scanner) {
        String sql = "{CALL fetchCharacters(?)}"; // Stored procedure call
        ArrayList<Integer> characterIDs = new ArrayList<>();
        boolean localrunning = true;

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            // Set the input parameter for the stored procedure
            callableStatement.setString(1, username);

            // Execute the stored procedure
            ResultSet resultSet = callableStatement.executeQuery();

            StringBuilder sb = new StringBuilder();

            // Append the table header to the StringBuilder
            sb.append(String.format("%-5s %-15s %-10s %-15s %-10s %-10s %-10s%n",
                    "Index", "Character Name", "XP", "Class", "Health", "Speed", "Stamina"));
            sb.append("--------------------------------------------------------------\n");

            int index = 1; // Start index at 1
            while (resultSet.next()) {
                int charID = resultSet.getInt("charID");
                String charName = resultSet.getString("charName");
                int xp = resultSet.getInt("xp");
                String className = resultSet.getString("name");
                int health = resultSet.getInt("health");
                int speed = resultSet.getInt("speed");
                int stamina = resultSet.getInt("stamina");

                // Append character details to the StringBuilder
                sb.append(String.format("%-5d %-15s %-10d %-15s %-10d %-10d %-10d%n",
                        index, charName, xp, className, health, speed, stamina));

                // Store charID in the ArrayList
                characterIDs.add(charID);
                index++;
            }

            // Optional: Print the StringBuilder's content to the console (if needed)
            System.out.println(sb.toString());

            // Reuse the StringBuilder's content later
            String formattedOutput = sb.toString();

            while(localrunning) {
                System.out.println(formattedOutput);
                System.out.println("\n Select character to manage.");
                System.out.println("Or enter 0 to exit this menu.");
                int choice = scanner.nextInt();
                if (choice == 0) {
                    localrunning = false;
                } else {
                    try {
                        int charID = characterIDs.get(choice-1);
                        manageChar(connection, scanner, charID);
                    } catch (Exception e) {
                        System.out.println("Invalid choice, try again.");
                    }
                }

            }

        } catch (SQLException e) {
            e.printStackTrace(); // Print error details
        }
        
        System.out.println(characterIDs.toString());

        return characterIDs; // Return the ArrayList of character IDs
    }
    public static void manageChar(Connection connection, Scanner scanner, int charID) {
        boolean localrunning = true;
        String username = fetchCharacterName(connection, charID);
        System.out.println("Managing " + username);
        while(localrunning) {
            System.out.println("\n Character Menu");
            System.out.println("1. View Info");
            System.out.println("2. View Completed Quests");
            System.out.println("3. View Inventory");
            System.out.println("4. Register Completed Quest");
            System.out.println("5. View Guild");
            System.out.println("6. Join Guild");
            System.out.println("7. Leave Guild");
            System.out.println("8. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    fetchCharacterInfo(connection, charID);
                    break;
                case 2:
                    fetchCompletedQuests(connection, charID);
                    break;
                case 3:
                    fetchInventory(connection, charID);
                    break;
                case 4:
                    completeQuest(connection, scanner, charID);
                    break;
                case 8:
                    localrunning = false;
                    break;
                case 5:
                    fetchGuild(connection, charID);
                    break;
                case 6:
                    joinGuild(connection, scanner, charID);
                    break;
                case 7: 
                    leaveGuild(connection, charID);
                    break;
                default:
                    System.out.println("Please input a valid choice.");
                    break;
            }
        }
    }

    public static void leaveGuild(Connection connection, int charID) {
        String sql = "{CALL leaveGuild(?)}"; // Stored procedure call

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            // Set the input parameter
            callableStatement.setInt(1, charID);

            // Execute the stored procedure
            callableStatement.execute();

            // Success message
            System.out.println("You have successfully left the guild.");
        } catch (SQLException e) {
            if (e.getErrorCode() == 50000) {
                System.out.println("Error: " + e.getMessage());
            } else {
                // Handle other SQL exceptions
                e.printStackTrace();
            }
        }
    }

    public static void joinGuild(Connection connection, Scanner scanner, int charID) {
        // Check if the character is already in a guild
        String checkGuildSQL = "SELECT guildID FROM guildMember WHERE charID = ?";
        String fetchGuildsSQL = "SELECT g.guildID, c.charName as leader, dateFounded, reputation FROM guild g JOIN character c on c.charID = g.leaderCharID";

        try {
            // Check if the character is already in a guild
            try (PreparedStatement checkStmt = connection.prepareStatement(checkGuildSQL)) {
                checkStmt.setInt(1, charID);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    System.out.println("You are already in a guild. You must leave your current guild before joining another.");
                    return;
                }
            }

            // Fetch and display available guilds
            HashMap<Integer, Integer> guildMap = new HashMap<>(); // Map index to guildID
            try (PreparedStatement fetchStmt = connection.prepareStatement(fetchGuildsSQL)) {
                ResultSet rs = fetchStmt.executeQuery();

                // Print the available guilds
                System.out.printf("%-5s %-20s%n", "Index", "Guild Leader");
                System.out.println("-------------------------");

                int index = 1;
                while (rs.next()) {
                    int guildID = rs.getInt("guildID");
                    String guildName = rs.getString("leader");

                    System.out.printf("%-5d %-20s%n", index, guildName);
                    guildMap.put(index, guildID);
                    index++;
                }
            }

            // Prompt user to select a guild
            System.out.println("\nSelect a guild by index:");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (guildMap.containsKey(choice)) {
                int selectedGuildID = guildMap.get(choice);

                // Call the joinGuild stored procedure
                String procCall = "{CALL joinGuild(?, ?)}";
                try (CallableStatement cs = connection.prepareCall(procCall)) {
                    cs.setInt(1, charID);
                    cs.setInt(2, selectedGuildID);

                    cs.execute();
                    System.out.println("You have successfully joined the guild!");
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print error details for debugging
        }
    }


    public static void fetchGuild(Connection connection, int charID) {
        String sql = "{CALL fetchGuild(?)}"; // Stored procedure call

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            // Set the input parameter for the stored procedure
            callableStatement.setInt(1, charID);

            // Execute the stored procedure
            ResultSet resultSet = callableStatement.executeQuery();

            // Print the table header
            System.out.printf("%-20s %-20s %-10s%n", "Leader Name", "Date Founded", "Reputation");
            System.out.println("-------------------------------------------------------------");

            // Process the result set
            if (resultSet.next()) {
                String leaderName = resultSet.getString("leader");
                Date dateFounded = resultSet.getDate("dateFounded");
                int reputation = resultSet.getInt("reputation");

                // Print the guild information
                System.out.printf("%-20s %-20s %-10d%n", leaderName, dateFounded.toString(), reputation);
            } else {
                System.out.println("No guild found for the given character ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print error details for debugging
        }
    }

    public static void completeQuest(Connection connection, Scanner scanner, int charID) {
        String sql = "SELECT q.questID, q.objective, q.minXp, q.xpReward, i.name, i.attack, i.defense " +
                     "FROM quest q " +
                     "JOIN item i ON i.itemID = q.itemRewardID";
    
        HashMap<Integer, Integer> questMap = new HashMap<>(); // Map index to questID
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Print the table header
            System.out.printf("%-5s %-50s %-10s %-10s %-15s %-10s %-10s%n",
                    "Index", "Objective", "Min XP", "XP Reward", "Item Name", "Attack", "Defense");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
    
            int index = 1; // Initialize index for user selection
            while (resultSet.next()) {
                int questID = resultSet.getInt("questID");
                String objective = resultSet.getString("objective");
                int minXp = resultSet.getInt("minXp");
                int xpReward = resultSet.getInt("xpReward");
                String itemName = resultSet.getString("name");
                int attack = resultSet.getInt("attack");
                int defense = resultSet.getInt("defense");
    
                // Print each quest's details with an index
                System.out.printf("%-5d %-50s %-10d %-10d %-15s %-10d %-10d%n",
                        index, objective, minXp, xpReward, itemName, attack, defense);
    
                // Map the index to the questID
                questMap.put(index, questID);
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print error details
            return; // Exit the method on failure
        }
    
        boolean localRunning = true;
        while (localRunning) {
            // Prompt the user to select a quest
            System.out.println("\nSelect Quest to complete by index:");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            if (questMap.containsKey(choice)) {
                int selectedQuestID = questMap.get(choice);
    
                try {
                    // Fetch minXp for the selected quest
                    int minXP = 0;
                    String xpQuery = "SELECT minXp FROM quest WHERE questID = ?";
                    try (PreparedStatement xpStmt = connection.prepareStatement(xpQuery)) {
                        xpStmt.setInt(1, selectedQuestID);
                        ResultSet xpRs = xpStmt.executeQuery();
                        if (xpRs.next()) {
                            minXP = xpRs.getInt("minXp");
                        }
                    }
    
                    // Fetch current XP for the character
                    int charXP = 0;
                    String charXpQuery = "SELECT xp FROM character WHERE charID = ?";
                    try (PreparedStatement charXpStmt = connection.prepareStatement(charXpQuery)) {
                        charXpStmt.setInt(1, charID);
                        ResultSet charXpRs = charXpStmt.executeQuery();
                        if (charXpRs.next()) {
                            charXP = charXpRs.getInt("xp");
                        }
                    }
    
                    // Check if character has enough XP
                    if (charXP >= minXP) {
                        // Call the stored procedure
                        String procCall = "{CALL questCompletion(?, ?)}";
    
                        connection.setAutoCommit(false); // Start transaction
                        try (CallableStatement cs = connection.prepareCall(procCall)) {
                            cs.setInt(1, charID);
                            cs.setInt(2, selectedQuestID);
    
                            cs.execute(); // Execute the stored procedure
                            connection.commit(); // Commit the transaction
                            System.out.println("Quest completed successfully!");
                        } catch (SQLException e) {
                            connection.rollback(); // Roll back on error
                            System.out.println("Transaction rolled back.");
                            e.printStackTrace();
                        } finally {
                            connection.setAutoCommit(true); // Reset auto-commit mode
                        }
    
                        localRunning = false; // Exit the loop on successful completion
                    } else {
                        System.out.println("Your XP is too low to complete this quest!");
                    }
    
                } catch (SQLException e) {
                    e.printStackTrace(); // Print error details
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    

    public static void fetchInventory(Connection connection, int charID) {
        String sql = "{CALL fetchInventory(?)}"; // Stored procedure call

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            // Set the input parameter for the stored procedure
            callableStatement.setInt(1, charID);

            // Execute the stored procedure
            ResultSet resultSet = callableStatement.executeQuery();

            // Print the inventory details
            System.out.printf("%-20s %-10s %-10s %-10s %-15s %-10s%n",
                    "Item Name", "Attack", "Defense", "CritChance", "Type", "Quantity");
            System.out.println("--------------------------------------------------------------------");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int attack = resultSet.getInt("attack");
                int defense = resultSet.getInt("defense");
                double critChance = resultSet.getDouble("critChance");
                String type = resultSet.getString("type");
                int quantity = resultSet.getInt("quantity");

                // Print each item's details
                System.out.printf("%-20s %-10d %-10d %-10.2f %-15s %-10d%n",
                        name, attack, defense, critChance, type, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print error details
        }
    }

    public static void fetchCharacterInfo(Connection connection, int charID) {
        String sql = "{CALL fetchCharacterInfo(?)}";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            // Set the input parameter for the stored procedure
            callableStatement.setInt(1, charID);

            // Execute the stored procedure
            ResultSet resultSet = callableStatement.executeQuery();

            // Print the results
            System.out.printf("%-20s %-20s %-10s %-10s %-10s%n",
                    "Char. Name", "Class Name", "Health", "Stamina", "Speed");
            System.out.println("----------------------------------------------------------------------------");

            while (resultSet.next()) {
                String name = resultSet.getString("charName");
                String className = resultSet.getString("className");
                int health = resultSet.getInt("health");
                int stamina = resultSet.getInt("stamina");
                int speed = resultSet.getInt("speed");

                // Print each quest's details
                System.out.printf("%-20s %-20s %-10d %-10d %-10d%n",
                        name, className, health, stamina, speed);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print error details
        }
    }

    public static String fetchCharacterName(Connection connection, int charID) {
        String sql = "SELECT charName FROM character WHERE charID = ?";
        String characterName = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set the input parameter
            preparedStatement.setInt(1, charID);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the character's name
                characterName = resultSet.getString("charName");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print error details
        }

        return characterName;
    }


    public static void fetchCompletedQuests(Connection connection, int charID) {
        String sql = "{CALL completedQuests(?)}"; // Stored procedure call

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            // Set the input parameter for the stored procedure
            callableStatement.setInt(1, charID);

            // Execute the stored procedure
            ResultSet resultSet = callableStatement.executeQuery();

            // Print the results
            System.out.printf("%-50s %-10s %-10s %-15s %-10s %-10s%n",
                    "Objective", "Min XP", "XP Reward", "Item Name", "Attack", "Defense");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                String objective = resultSet.getString("objective");
                int minXp = resultSet.getInt("minXp");
                int xpReward = resultSet.getInt("xpReward");
                String itemName = resultSet.getString("name");
                int attack = resultSet.getInt("attack");
                int defense = resultSet.getInt("defense");

                // Print each quest's details
                System.out.printf("%-50s %-10d %-10d %-15s %-10d %-10d%n",
                        objective, minXp, xpReward, itemName, attack, defense);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print error details
        }
    }
}


