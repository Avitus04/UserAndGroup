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

@Path("/api/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    public static final Map<String, String> USERS = new HashMap<>();




    @POST
    public Response login(Credentials credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        membershipServiceImpl memberService = new membershipServiceImpl();
        password = memberService.encrypt(password);
        // Créez une instance de la classe UserService
        UserService userService = new UserService();
        String roleBDD = userService.getUserRole(username, password);

        String usernameBDD = userService.getUserName(username);
        String passwordBDD = userService.getUserPassword(username);
        System.out.println("access : \n"+ roleBDD); //test debug


        if (username == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "Nom d'utilisateur et mot de passe requis"))
                    .build();
        }


        else if (passwordBDD != null && passwordBDD.equals(password)) {

            RouteBlockingFilter2 routeBlockingFilter2 = new RouteBlockingFilter2();
            String name = routeBlockingFilter2.keyNameUser();
            userService.changeStatus(false, name);
            USERS.clear();
            // Ajout des utilisateurs dans le MAP
            USERS.put(usernameBDD, passwordBDD);
            //Création d'une instance de la classe RouteBlockingFilter , ce qui permet d'utiliser une de ces fonctions
            RouteBlockingFilter routeBlockingFilter = new RouteBlockingFilter();
            // Change la condition , ce qui permet d'avoir accès aux autres commandes
            routeBlockingFilter.setConditionMet(true);
            userService.changeStatus(true, usernameBDD);

            tokenServiceImpl tokenImpl = new tokenServiceImpl();
            token tok = tokenImpl.create();
            int result = userService.setUserToken(username, tok.token_id);

            if (result == 0) {return Response.notModified().build();}

            result = userService.setUserLastConnection(username, tok.date_created);

            if (result == 0) {return Response.notModified().build();}

            return Response.ok(Map.of("message", "Connexion réussie", "username", username, "uuid", userService.getUuid_user())).build();
        } else {
            System.out.println("password : "+ passwordBDD);
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

