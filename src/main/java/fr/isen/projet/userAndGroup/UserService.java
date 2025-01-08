package fr.isen.projet.userAndGroup;

import java.sql.*;
import java.util.Date;


public class UserService {

    public String getUserRole(String username, String password) {
        String query = "SELECT user_profile.access_level\n" +
                "FROM membership\n" +
                "JOIN user_profile ON membership.profile_id = user_profile.profile_id\n"+
                "WHERE username = ? AND passwd = ? ;"; // nomrally : WHERE username = ?


        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                //return rs.getString("access_level");
                String access = rs.getString("access_level");
                if (access.equals("1"))
                {
                    System.out.println("You are an admin ! Congratulation\n");
                }
                else if (access.equals("2"))
                {
                    System.out.println("You are a CE ! Congratulation\n");
                }
                else if (access.equals("3"))
                {
                    System.out.println("You are a member ! Congratulation\n");
                }
                return rs.getString("access_level");
            } else {
                return null; // Aucun rôle trouvé
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getUserName(String username, String passwd) {
        String query = "SELECT username\n" +
                "FROM membership\n" +
                "WHERE username = ? AND passwd = ? ;";


        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, passwd);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("username");
                return name;
            } else {
                return null; // Aucun utilisateur trouvé
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getUserPassword(String username, String passwd) {
        String query = "SELECT passwd\n" +
                "FROM membership\n" +
                "WHERE username = ? AND passwd = ? ;";


        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, passwd);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String password = rs.getString("passwd");
                return password;


            } else {
                System.out.println("Aucun utilisateur trouve \n");
                return null; // Aucun utilisateur trouvé
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUserToken(String username) {
        String query = "SELECT token_id\n" +
                "FROM membership\n" +
                "WHERE username = ?;";


        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String tok = rs.getString("token_id");
                return tok;

            } else {
                System.out.println("Aucun utilisateur trouve \n");
                return null; // Aucun utilisateur trouvé
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int setUserToken(String username, String tokenID) {
        String query = "UPDATE membership SET token_id = ? WHERE username = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, tokenID);
            stmt.setString(2, username);
            stmt.executeUpdate();;

            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int setUserLastConnection(String username, Date dateLastConnection) {
        String query = "UPDATE membership SET date_last_connection = ? WHERE username = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, new Timestamp(dateLastConnection.getTime())); // Date de création
            stmt.setString(2, username);
            stmt.executeUpdate();;

            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String changeStatus(boolean status,String username)
    {
        String query = "UPDATE membership SET status_user = ? WHERE username = ?\n";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, status);
            stmt.setString(2, username);
            stmt.executeUpdate();;

            return "OK";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
}



