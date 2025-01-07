package fr.isen.projet.userAndGroup.impl.services;

import fr.isen.projet.userAndGroup.interfaces.models.membership;
import fr.isen.projet.userAndGroup.interfaces.models.token;
import fr.isen.projet.userAndGroup.interfaces.services.membershipService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class membershipServiceImpl implements membershipService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/projet";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "1234";

    @Override
    public List<membership> getAll() {
        List<membership> memberships = new ArrayList<>();
        String query = "SELECT * FROM membership";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                membership member = new membership();
                member.membership_id = resultSet.getString("membership_id");
                member.username = resultSet.getString("username");
                member.passwd = resultSet.getString("passwd");
                member.address_id = resultSet.getString("address_id");
                member.date_created = resultSet.getDate("date_created");
                member.date_last_connection = resultSet.getDate("date_last_connection");
                member.status_user = resultSet.getBoolean("status_user");
                member.token_id = resultSet.getString("token_id");
                member.profile_id = resultSet.getString("profile_id");

                memberships.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return memberships;
    }

    @Override
    public membership getByID(String ID) {
        membership member = null;
        String query = "SELECT * FROM membership WHERE membership_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    member = new membership();
                    member.membership_id = resultSet.getString("membership_id");
                    member.username = resultSet.getString("username");
                    member.passwd = resultSet.getString("passwd");
                    member.address_id = resultSet.getString("address_id");
                    member.date_created = resultSet.getDate("date_created");
                    member.date_last_connection = resultSet.getDate("date_last_connection");
                    member.status_user = resultSet.getBoolean("status_user");
                    member.token_id = resultSet.getString("token_id");
                    member.profile_id = resultSet.getString("profile_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

    @Override
    public String add(membership data) {
        String query = "INSERT INTO membership (membership_id, address_id, profile_id, token_id, username, passwd, date_created, date_last_connection, status_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, data.membership_id);
            preparedStatement.setString(2, data.address_id);
            preparedStatement.setString(3, data.profile_id);
            preparedStatement.setString(4, data.token_id);
            preparedStatement.setString(5, data.username);
            preparedStatement.setString(6, data.passwd);
            preparedStatement.setDate(7, new Date(data.date_created.getTime()));
            preparedStatement.setDate(8, new Date(data.date_last_connection.getTime()));
            preparedStatement.setBoolean(9, data.status_user);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0 ? "Membership added successfully" : "Failed to add membership";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String update(String ID, membership data) {
        String query = "UPDATE membership SET membership_id = ?, address_id = ?, profile_id = ?, token_id = ?, username = ?, passwd = ?, date_created = ?, date_last_connection = ?, status_user = ? WHERE membership_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, data.membership_id);
            preparedStatement.setString(2, data.address_id);
            preparedStatement.setString(3, data.profile_id);
            preparedStatement.setString(4, data.token_id);
            preparedStatement.setString(5, data.username);
            preparedStatement.setString(6, data.passwd);
            preparedStatement.setDate(7, new Date(data.date_created.getTime()));
            preparedStatement.setDate(8, new Date(data.date_last_connection.getTime()));
            preparedStatement.setBoolean(9, data.status_user);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0 ? "Membership updated successfully" : "Failed to update membership";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String removeByID(String ID) {
        String query = "DELETE FROM membership WHERE membership_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0 ? "Membership removed successfully" : "Failed to remove membership";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public token connection(String login, String password) {
        return null;
    }

}