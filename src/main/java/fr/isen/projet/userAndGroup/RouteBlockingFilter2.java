package fr.isen.projet.userAndGroup;

import fr.isen.projet.userAndGroup.impl.services.tokenServiceImpl;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;

import java.util.Set; //for set<String>
import java.util.Collection; //for Collection<String>





@Provider

@Priority(Priorities.AUTHORIZATION) // Priorité élevée (ex. : AUTHENTICATION = 1000)
public class RouteBlockingFilter2 implements ContainerRequestFilter {

    String name = new String(); //Keep the name of the user from the map
    String pwd = new String(); //Keep the password of the user from the map

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

    // method to identify the metjod that the user use and keep it in variable "methodName"
    String methodName = new String();
    public void method(ContainerRequestContext context) {
        // Obtenir le type de méthode HTTP
        String httpMethod = context.getMethod();

        // Identifier le type d'opération CRUD
        switch (httpMethod) {
            case "POST":
                System.out.println("Requête de type CREATE (POST)");
                methodName = httpMethod;
                break;
            case "GET":
                System.out.println("Requête de type READ (GET)");
                methodName = httpMethod;
                break;
            case "PUT":
                System.out.println("Requête de type UPDATE (PUT)");
                methodName = httpMethod;
                break;
            case "DELETE":
                System.out.println("Requête de type DELETE (DELETE)");
                methodName = httpMethod;
                break;
            default:
                System.out.println("Autre type de requête : " + httpMethod);
                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Error: Wrong Method")
                        .build());
        }
    }


    @Override
    public void filter(ContainerRequestContext context) {
        //---------- Récupération Access_level ------------------------------
        keyuser(); // Get the name and password use
        UserService userService = new UserService();
        String roleBDD = userService.getUserRole(name, pwd);
        method(context); // Get the method use

        String path = context.getUriInfo().getPath();

        // Vérifiez si le chemin correspond à la route accessible
        if (path.equals("/api/login")) {
            //enlever le user
            return; // Autorise l'accès à cette route
        }

        tokenServiceImpl tokenServ = new tokenServiceImpl();

        if (!tokenServ.checkToken(userService.getUserToken(name)))
        {
            // Bloquer la requête avec un code 403 (Forbidden)
            userService.changeStatus(false, name);
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: your token has expired")
                    .build());
            return;
        }

        if (path.equals("/status") && roleBDD.equals("1")) {
            return; // Autorise l'accès à cette route
        }

        else if (path.startsWith("/membership/")) { // get id
            String id = path.split("/")[2];
            //System.out.println("id extrait : " + id);

            if (methodName.equals("GET")){
                if (roleBDD.equals("1"))
                {
                    //System.out.println("ça marche get");
                    System.out.println("Method Get with path : /membership/{id}\n id : "+ id + "\nAs an Admin\n");
                    return;
                }
                else if (roleBDD.equals("2") || roleBDD.equals("3")) {

                    if(id.equals(name)){
                        //System.out.println("id : " + id);
                        System.out.println("Method Get with path : /membership/{id}\n id : "+ id + "\nAs a CE or a member\n");
                        return;
                    }
                }
                //System.out.println("username cle : "+ name);
                //System.out.println("username role : "+ roleBDD);
                //System.out.println("username test : "+ LoginResource.USERS);

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE itself and member itself only")
                        .build());
                return;
            }
            else if(methodName.equals("PUT")){
                if (roleBDD.equals("1"))
                {
                    System.out.println("Method Put with path : /membership/{id}\n id : "+ id + "\nAs an Admin\n");
                    return;
                }
                else if (roleBDD.equals("3")) {
                    if(id.equals(name)){
                        /*String membre = "SELECT profile_id FROM membership WHERE id = ?";
                        String body = getRequestProfileId();
                        if(membre.equals(body)){

                        }*/
                        //System.out.println("ça marche PUT");
                        System.out.println("Method Put with path : /membership/{id}\n id : "+ id + "\nAs a member\n");
                        return;
                    }
                }
                //System.out.println("username cle : "+ name);
                //System.out.println("username role : "+ roleBDD);
                //System.out.println("username test : "+ LoginResource.USERS);

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins and member itself only")
                        .build());
                return;
            }
            else if (methodName.equals("DELETE")){
                if (roleBDD.equals("1"))
                {
                    //System.out.println("ça marche delete");
                    System.out.println("Method Delete with path : /membership/{id}\n id : "+ id + "\nAs an Admin\n");
                    return;
                }
                //System.out.println("username cle : "+ name);
                //System.out.println("username role : "+ roleBDD);
                //System.out.println("username test : "+ LoginResource.USERS);

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins only")
                        .build());
                return;
            }
        }
        else if (path.equals("/membership")) { //get
            if (methodName.equals("GET")){
                if (roleBDD.equals("1"))
                {
                    //System.out.println("ça marche GET");
                    System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }
                //System.out.println("username cle : "+ name);
                //System.out.println("username role : "+ roleBDD);
                //System.out.println("username test : "+ LoginResource.USERS);

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins only")
                        .build());
                return;
            }
            else if (methodName.equals("POST")){
                if (roleBDD.equals("1") || roleBDD.equals("2"))
                {
                    //System.out.println("ça marche post");
                    System.out.println("Method Post with path : /membership\nAs an Admin or a CE\n");
                    return;
                }
                //System.out.println("username cle : "+ name);
                //System.out.println("username role : "+ roleBDD);
                //System.out.println("username test : "+ LoginResource.USERS);

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins and CE only")
                        .build());
                return;
            }
        }

        //Get all user: admin & CE & member can do it
        else if (path.equals("/user_profile") && methodName.equals("GET")) {
            if (roleBDD.equals("1") || roleBDD.equals("2") || roleBDD.equals("3"))
            {
                System.out.println("Method Get with path : /user_profile\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Error")
                    .build());
            return;
        }

        //Get user by ID: admin & CE & member can do it
        else if (path.startsWith("/user_profile/") && methodName.equals("GET")) {
            String id = path.split("/")[2]; // not useful

            if (roleBDD.equals("1") || roleBDD.equals("2") || roleBDD.equals("3"))
            {
                System.out.println("Method Get with path : /user_profile/{id}\n id : "+ id + "\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Error")
                    .build());
            return;
        }

        //Post a user: admin can do it
        else if (path.equals("/user_profile") && methodName.equals("POST")) {
            if (roleBDD.equals("1"))
            {
                System.out.println("Method Post with path : /user_profile\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admin only")
                    .build());
            return;
        }

        //Put a user: admin can do it
        else if (path.startsWith("/user_profile/") && methodName.equals("PUT")) {
            String id = path.split("/")[2]; // not useful
            if (roleBDD.equals("1"))
            {
                System.out.println("Method Put with path : /user_profile/{id}\n id : " + id + "\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admin only")
                    .build());
            return;
        }

        //Delete a user: admin can do it
        else if (path.startsWith("/user_profile/") && methodName.equals("DELETE")) {
            String id = path.split("/")[2]; // not useful
            if (roleBDD.equals("1"))
            {
                System.out.println("Method Put with path : /user_profile/{id}\n id : " + id + "\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admin only")
                    .build());
            return;
        }
        else
        {
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access Block: Error")
                    .build());
            return;
        }

    }


}
