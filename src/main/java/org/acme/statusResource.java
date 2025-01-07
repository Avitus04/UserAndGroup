package fr.isen.projet.userAndGroup;
//Mettez le nom et le chemin de votre dossier

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/status")
public class statusResource {
    @GET
    public String getStatus() throws JsonProcessingException {
        String state = "OK"; // OK ou KO ou Dégradé
        // L'état dégradé veut dire que votre code interne fonctionne mais que vous attendez un code dont vous êtes dépendants, donc d'un autre groupe
        String version = "1.0";
        int count = 42; // Remplacez par une requête réelle pour compter les éléments de votre BDD

        Map<String, Object> statusResponse = new HashMap<>();
        statusResponse.put("state", state);
        statusResponse.put("count", count);
        statusResponse.put("version", version);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(statusResponse);
    }

}
