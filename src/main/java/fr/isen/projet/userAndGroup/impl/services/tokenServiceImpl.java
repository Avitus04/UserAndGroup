package fr.isen.projet.userAndGroup.impl.services;

import fr.isen.projet.userAndGroup.interfaces.models.membership;
import fr.isen.projet.userAndGroup.interfaces.models.token;
import fr.isen.projet.userAndGroup.interfaces.services.tokenService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Path("/") // Chemin de base de la ressource REST
@Produces(MediaType.APPLICATION_JSON) // Les méthodes retourneront du JSON
@Consumes(MediaType.APPLICATION_JSON) // Les méthodes accepteront du JSON
public class tokenServiceImpl implements tokenService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/projet";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "1234";

    @Override
    public boolean checkToken(String token) {
            String querySelect = "SELECT token_id, date_created, date_expiration, status_token FROM token WHERE token_id = ?";
            String queryUpdateStatus = "UPDATE token SET status_token = ?, date_expiration = ? WHERE token_id = ?";
            String querySetStatusFalse = "UPDATE token SET status_token = ? WHERE token_id = ?";

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement selectStmt = connection.prepareStatement(querySelect)) {

                // Vérifier si le token existe dans la base de données
                selectStmt.setString(1, token);
                try (ResultSet resultSet = selectStmt.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new IllegalArgumentException("Token not found in the database.");
                    }

                    // Récupérer les informations du token
                    Timestamp dateCreated = resultSet.getTimestamp("date_created");
                    Timestamp dateExpiration = resultSet.getTimestamp("date_expiration");
                    boolean statusToken = resultSet.getBoolean("status_token");

                    if (!statusToken)
                    {
                        return false;
                    }

                    // Vérifier si le token est déjà expiré


                    Date date_current = new Date();

                    Calendar calendar0 = Calendar.getInstance();
                    calendar0.setTime(date_current);
                    calendar0.add(Calendar.HOUR, 1);

                    date_current = calendar0.getTime();

                    if (dateExpiration.before(date_current)) {
                        // Mettre le statut à false si expiré
                        try (PreparedStatement updateStmt = connection.prepareStatement(querySetStatusFalse)) {
                            updateStmt.setBoolean(1, false);
                            updateStmt.setString(2, token);
                            updateStmt.executeUpdate();
                        }
                        return false;
                    }

                    // Ajouter 20 minutes à la date d'expiration
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateExpiration);
                    calendar.add(Calendar.MINUTE, 20);

                    // Vérifier que la nouvelle date d'expiration ne dépasse pas date_created + 4 heures
                    Calendar maxExpirationCalendar = Calendar.getInstance();
                    maxExpirationCalendar.setTime(dateCreated);
                    maxExpirationCalendar.add(Calendar.HOUR, 4);

                    if (calendar.after(maxExpirationCalendar)) {
                        calendar.setTime(maxExpirationCalendar.getTime());
                    }

                    Timestamp newExpirationDate = new Timestamp(calendar.getTimeInMillis());

                    // Mettre à jour la date d'expiration dans la base de données
                    try (PreparedStatement updateStmt = connection.prepareStatement(queryUpdateStatus)) {
                        updateStmt.setBoolean(1, true);
                        updateStmt.setTimestamp(2, newExpirationDate);
                        updateStmt.setString(3, token);
                        updateStmt.executeUpdate();
                    }

                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Database error occurred while checking the token.");
            }
        }


    @Override
    public token create() {
        token tok = new token();
        byte[] bytes = new byte[32]; // 32 octets = 64 caractères hexadécimaux
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);

        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }

        String tokenValue  = hexString.toString();

        tok.token_id = tokenValue;

        Date dateCreated = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateCreated);
        calendar.add(Calendar.HOUR, 1);

        dateCreated = calendar.getTime();
        // Calculer la date d'expiration (une heure après)
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(dateCreated);
        calendar2.add(Calendar.HOUR, 1);
        Date dateExpiration = calendar2.getTime();

        tok.date_created = dateCreated;
        tok.date_expiration = dateExpiration;
        tok.status_token = true;

        // Insérer dans la base de données
        String query = "INSERT INTO token (token_id, date_created, date_expiration, status_token) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, tokenValue);
            preparedStatement.setTimestamp(2, new Timestamp(dateCreated.getTime())); // Date de création
            preparedStatement.setTimestamp(3, new Timestamp(dateExpiration.getTime())); // Date d'expiration
            preparedStatement.setBoolean(4, true); // Statut actif par défaut

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Token successfully created and stored in the database.");
            }
        } catch (SQLException e) {
            System.err.println("Error while inserting token into the database: " + e.getMessage());
        }

        return tok;
    }

    @GET
    @Path("/token")
    public List<token> getAll() {
        List<token> tokens = new ArrayList<>();
        String query = "SELECT * FROM token";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                token tok = new token();
                tok.token_id = resultSet.getString("token_id");
                tok.date_created = resultSet.getTimestamp("date_created");
                tok.date_expiration = resultSet.getTimestamp("date_expiration");
                tok.status_token = resultSet.getBoolean("status_token");


                tokens.add(tok);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tokens;
    }
}
