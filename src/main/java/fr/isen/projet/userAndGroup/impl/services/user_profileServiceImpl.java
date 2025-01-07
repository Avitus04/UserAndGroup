package fr.isen.projet.userAndGroup.impl.services;


import fr.isen.projet.userAndGroup.interfaces.enums.access_level;
import fr.isen.projet.userAndGroup.interfaces.models.user_profile;
import fr.isen.projet.userAndGroup.interfaces.services.user_profileService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class user_profileServiceImpl implements user_profileService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/projet";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "1234";

    @Override
    public List<user_profile> getAll() {
        List<user_profile> userProfiles = new ArrayList<>();
        String query = "SELECT * FROM user_profile";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                user_profile profile = new user_profile();
                profile.profile_id = resultSet.getString("profile_id");
                profile.description = resultSet.getString("description");

                int access = resultSet.getInt("access_level");

                if (access == 1)
                {
                    profile.access_level = access_level.ADMIN;
                }
                else if (access == 2)
                {
                    profile.access_level = access_level.CE;
                }
                else if (access == 3)
                {
                    profile.access_level = access_level.MEMBER;
                }
                userProfiles.add(profile);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userProfiles;
    }

    @Override
    public user_profile getByID(String ID) {
        user_profile profile = null;
        String query = "SELECT * FROM user_profile WHERE profile_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    profile = new user_profile();
                    profile.profile_id = resultSet.getString("profile_id");
                    profile.description = resultSet.getString("description");
                    int access = resultSet.getInt("access_level");

                    if (access == 1)
                    {
                        profile.access_level = access_level.ADMIN;
                    }
                    else if (access == 2)
                    {
                        profile.access_level = access_level.CE;
                    }
                    else if (access == 3)
                    {
                        profile.access_level = access_level.MEMBER;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profile;
    }

    @Override
    public String add(user_profile data) {
        String query = "INSERT INTO user_profile (profile_id, description, access_level) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, data.profile_id);
            preparedStatement.setString(2, data.description);

            access_level access = data.access_level;
            if (access == access_level.ADMIN)
            {
                preparedStatement.setInt(3, 1);
            }
            else if (access == access_level.CE)
            {
                preparedStatement.setInt(3, 2);
            }
            else if (access == access_level.MEMBER)
            {
                preparedStatement.setInt(3, 3);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0 ? "User profile added successfully" : "Failed to add user profile";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String update(String ID, user_profile data) {
        String query = "UPDATE user_profile SET profile_id = ?, description = ?, access_level = ? WHERE profile_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, data.profile_id);
            preparedStatement.setString(2, data.description);
            access_level access = data.access_level;
            if (access == access_level.ADMIN)
            {
                preparedStatement.setInt(3, 1);
            }
            else if (access == access_level.CE)
            {
                preparedStatement.setInt(3, 2);
            }
            else if (access == access_level.MEMBER)
            {
                preparedStatement.setInt(3, 3);
            }
            preparedStatement.setString(4, ID);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0 ? "User profile updated successfully" : "Failed to update user profile";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String removeByID(String ID) {
        String query = "DELETE FROM user_profile WHERE profile_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0 ? "User profile removed successfully" : "Failed to remove user profile";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

}
