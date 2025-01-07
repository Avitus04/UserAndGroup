package fr.isen.projet.userAndGroup;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/status")
public class statusResource {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/user_and_group"; // URL de la base
    private static final String DB_USER = "admin"; // Nom d'utilisateur
    private static final String DB_PASSWORD = "1234"; // Mot de passe

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getStatus() throws JsonProcessingException {
        String state = "OK"; // OK, KO ou Dégradé
        String version = "1.0";

        Map<String, Long> tableCounts = new HashMap<>();
        try {
            tableCounts = getTableCounts();
        } catch (Exception e) {
            state = "KO"; // Indiquer une erreur si la base de données ne répond pas
            e.printStackTrace();
        }

        Map<String, Object> statusResponse = new HashMap<>();
        statusResponse.put("state", state);
        statusResponse.put("version", version);
        statusResponse.put("tableCounts", tableCounts);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(statusResponse);
    }

    private Map<String, Long> getTableCounts() throws Exception {
        Map<String, Long> tableCounts = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Récupérer la liste des tables
            try (Statement tableStatement = connection.createStatement();
                 ResultSet tables = tableStatement.executeQuery("SHOW TABLES")) {

                while (tables.next()) {
                    String tableName = tables.getString(1);

                    // Effectuer un COUNT() pour chaque table avec une nouvelle instance de Statement
                    try (Statement countStatement = connection.createStatement();
                         ResultSet countResult = countStatement.executeQuery("SELECT COUNT(*) FROM `" + tableName + "`")) {

                        if (countResult.next()) {
                            tableCounts.put(tableName, countResult.getLong(1));
                        }
                    } catch (Exception e) {
                        System.err.println("Erreur lors du comptage pour la table " + tableName + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la connexion ou de l'exécution de la requête : " + e.getMessage());
            throw e;
        }

        return tableCounts;
    }
}
