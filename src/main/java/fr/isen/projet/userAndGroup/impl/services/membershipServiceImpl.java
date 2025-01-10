package fr.isen.projet.userAndGroup.impl.services;

import fr.isen.projet.userAndGroup.UserService;
import fr.isen.projet.userAndGroup.interfaces.models.membership;
import fr.isen.projet.userAndGroup.interfaces.models.token;
import fr.isen.projet.userAndGroup.interfaces.services.membershipService;

import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Date;

public class membershipServiceImpl implements membershipService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/projet";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "1234";

    @Override
    public List<membership> getAll() {
        List<membership> memberships = new ArrayList<>();
        String query = "SELECT * FROM membership;";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                membership member = new membership();
                member.uuid_user = resultSet.getString("uuid_user");
                member.username = resultSet.getString("username");
                member.passwd = resultSet.getString("passwd");
                member.uuid_address = resultSet.getString("uuid_address");
                member.date_created = resultSet.getTimestamp("date_created");
                member.date_last_connection = resultSet.getTimestamp("date_last_connection");
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
        String query = "SELECT * FROM membership WHERE uuid_user = ?;";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    member = new membership();
                    member.uuid_user = resultSet.getString("uuid_user");
                    member.username = resultSet.getString("username");
                    member.passwd = resultSet.getString("passwd");
                    member.uuid_address = resultSet.getString("uuid_address");
                    member.date_created = resultSet.getTimestamp("date_created");
                    member.date_last_connection = resultSet.getTimestamp("date_last_connection");
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
        String query = "INSERT INTO membership (uuid_user, uuid_address, profile_id, token_id, username, passwd, date_created, date_last_connection, status_user) VALUES (?, ?, ?, ?, ?, MD5(?), ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String uuid = UUID.randomUUID().toString();
            preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, data.uuid_address);
            preparedStatement.setString(3, data.profile_id);
            preparedStatement.setString(4, null);
            preparedStatement.setString(5, data.username);
            preparedStatement.setString(6, data.passwd);

            Date dateCurrent = new Date(); // Heure actuelle
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateCurrent);
            calendar.add(Calendar.HOUR, 1); // Ajouter 1 heure
            Date datePlusOneHour = calendar.getTime();

            preparedStatement.setTimestamp(7, new Timestamp(datePlusOneHour.getTime()));
            preparedStatement.setTimestamp(8, null);
            preparedStatement.setBoolean(9, false);



            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0 ? "Membership added successfully \nuuid: " + uuid : "Failed to add membership ";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String update(String ID, membership data) {
        String query = "UPDATE membership SET username = ?, passwd = MD5(?), profile_id = ? WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, data.username);
            preparedStatement.setString(2, data.passwd);
            preparedStatement.setString(3, data.profile_id);
            preparedStatement.setString(4, ID);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0 ? "Membership updated successfully" : "Failed to update membership";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String removeByID(String ID) {
        String query = "DELETE FROM membership WHERE uuid_user = ?";

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


    public String showAddress()
    {
        try {
            String apiUrl = "http://localhost:8083/addresses";
            return callExternalAPI(apiUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }



    private String callExternalAPI(String apiUrl) throws IOException {
        // Créer l'objet URL
        URL url = new URL(apiUrl);

        // Ouvrir la connexion HTTP
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Configurer la méthode en GET
        connection.setRequestMethod("GET");

        // Obtenir le code de réponse
        int responseCode = connection.getResponseCode();

        // Vérifier si le code de réponse est un succès (200-299)
        if (responseCode < 200 || responseCode > 299) {
            throw new RuntimeException("Échec de l'appel à l'API externe : " + apiUrl + " (Code : " + responseCode + ")");
        }

        // Lire et retourner la réponse
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            // Fermer la connexion
            connection.disconnect();
        }
    }


    @Override
    public token connection(String login, String password) {
        return null;
    }

}