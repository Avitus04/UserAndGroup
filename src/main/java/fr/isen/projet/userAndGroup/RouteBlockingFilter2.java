package fr.isen.projet.userAndGroup;

import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;

import java.util.Set; //pour le set<String>
import java.util.Collection;





@Provider

@Priority(Priorities.AUTHORIZATION) // Priorité élevée (ex. : AUTHENTICATION = 1000)
public class RouteBlockingFilter2 implements ContainerRequestFilter {

    String name = new String();
    String pwd = new String();

    //On récupère l'username et le mot de passe
    Set<String> keys = LoginResource.USERS.keySet();

    public void keyuser(){
        for (String key : keys){
            name = key;
        }

        Collection<String> values = LoginResource.USERS.values();

        for (String value : values){
            pwd = value;
        }
    }

    @Override
    public void filter(ContainerRequestContext context) {
        //---------- Récupération Access_level ------------------------------
        keyuser();
        UserService userService = new UserService();
        String roleBDD = userService.getUserRole(name, pwd);


        String path = context.getUriInfo().getPath();

        // Vérifiez si le chemin correspond à la route accessible
        if (path.equals("/api/login")) {
            return; // Autorise l'accès à cette route
        }


        if (path.equals("/membership/get")) {
            if (roleBDD.equals("1"))
            {
                System.out.println("username cle : "+ name);
                return;
            }
            System.out.println("username cle : "+ name);
            System.out.println("username role : "+ roleBDD);
            System.out.println("username test : "+ LoginResource.USERS);

            // Bloquer la requête avec un code 403 (Forbidden)
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins only")
                    .build());
            return;
        }

        if (path.equals("/membership/post")) {
            if (roleBDD.equals("1") || roleBDD.equals("2"))
            {
                System.out.println("ça marche post");
                return;
            }
            System.out.println("username cle : "+ name);
            System.out.println("username role : "+ roleBDD);
            System.out.println("username test : "+ LoginResource.USERS);

            // Bloquer la requête avec un code 403 (Forbidden)
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        if (path.equals("/membership/get/{id}")) {
            if (roleBDD.equals("1"))
            {
                System.out.println("ça marche post");
                return;
            }
            else if (roleBDD.equals("2") || roleBDD.equals("3")) {
                String check = "SELECT username\n" +
                        "FROM membership\n" +
                        "WHERE username = name ;";

                if(check.equals(name)){
                    return;
                }
            }
            System.out.println("username cle : "+ name);
            System.out.println("username role : "+ roleBDD);
            System.out.println("username test : "+ LoginResource.USERS);

            // Bloquer la requête avec un code 403 (Forbidden)
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins, CE itself and member itself only")
                    .build());
            return;
        }

        if (path.equals("/membership/put/{id}")) {
            if (roleBDD.equals("1"))
            {
                return;
            }
            else if (roleBDD.equals("3")) {
                String check = "SELECT username\n" +
                        "FROM membership\n" +
                        "WHERE username = name ;";

                if(check.equals(name)){
                    return;
                }
            }
            System.out.println("username cle : "+ name);
            System.out.println("username role : "+ roleBDD);
            System.out.println("username test : "+ LoginResource.USERS);

            // Bloquer la requête avec un code 403 (Forbidden)
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and member itself only")
                    .build());
            return;
        }

        if (path.equals("/membership/delete/{id}")) {
            if (roleBDD.equals("1"))
            {
                System.out.println("ça marche delete");
                return;
            }
            System.out.println("username cle : "+ name);
            System.out.println("username role : "+ roleBDD);
            System.out.println("username test : "+ LoginResource.USERS);

            // Bloquer la requête avec un code 403 (Forbidden)
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins only")
                    .build());
            return;
        }


/*
        if (!conditionMet) {
            context.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"message\": \"Action requise avant l'accès à cette ressource.\"}")
                    .build());
        }*/

    }


}
