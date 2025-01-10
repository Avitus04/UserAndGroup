package fr.isen.projet.userAndGroup;


import fr.isen.projet.userAndGroup.impl.services.membershipServiceImpl;
import fr.isen.projet.userAndGroup.impl.services.tokenServiceImpl;
import fr.isen.projet.userAndGroup.interfaces.models.token;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Path("/api/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {


    @POST
    public Response login(Credentials credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        membershipServiceImpl memberService = new membershipServiceImpl();
        // Créez une instance de la classe UserService
        UserService userService = new UserService();
        String roleBDD = userService.getUserRole(username, password);

        String checkIdentification = userService.getUserPassword(username, password);
        System.out.println("access : \n"+ roleBDD); //test debug


        if (username == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "Nom d'utilisateur et mot de passe requis"))
                    .build();
        }


        else if (Objects.equals(checkIdentification, username)) {

            userService.changeStatus(true, username);

            tokenServiceImpl tokenImpl = new tokenServiceImpl();
            token tok = tokenImpl.create();
            int result = userService.setUserToken(username, tok.token_id);

            if (result == 0) {return Response.notModified().build();}

            result = userService.setUserLastConnection(username, tok.date_created);

            if (result == 0) {return Response.notModified().build();}

            return Response.ok(Map.of("message", "Connexion réussie", "username", username, "token :", tok.token_id)).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("message", "Nom d'utilisateur ou mot de passe incorrect"))
                    .build();
        }
    }

    // Classe pour les informations d'identification
    public static class Credentials {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}

