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


    /*
    public void keyuser(){
        //On récupère l'username et le mot de passe
        Set<String> keys = LoginResource.USERS.keySet();

        for (String key : keys){
            name = key;
        }

        Collection<String> values = LoginResource.USERS.values();

        for (String value : values){
            pwd = value;
        }
    }*/
    public String keyNameUser()
    {
        Set<String> keys = LoginResource.USERS.keySet();

        for (String key : keys){
            name = key;
        }
        return name;
    }

    public String keyPwdUser()
    {
        Collection<String> values = LoginResource.USERS.values();

        for (String value : values){
            pwd = value;
        }
        return pwd;
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
        String name = keyNameUser(); // Get the name use in Map
        String pwd = keyPwdUser(); // Get the password use in Map
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
            userService.changeStatus(false, name);
            // Bloquer la requête avec un code 403 (Forbidden)
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

                    if(id.equals(userService.getUuid_user())){
                        //System.out.println("id : " + id);
                        System.out.println("Method Get with path : /membership/{id}\n id : "+ id + "\nAs a CE or a member\n");
                        return;
                    }
                }

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
                    if(id.equals(userService.getUuid_user())){
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

        //---------------------------------- CRM ------------------------------------
        //----------------------------------CRM - TICKET ----------------------------
        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/ticket") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /ticket\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/ticket/") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /ticket\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/ticket") && methodName.equals("POST")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /ticket\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/ticket") && methodName.equals("PUT")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /ticket\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/ticket/status/") && methodName.equals("PUT")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /ticket/status/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/ticket/user/") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /ticket/user/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/ticket/search") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /ticket/search?....\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/ticket/") && methodName.equals("DELETE")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /ticket/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }
        //----------------------------------CRM - TICKET_ACTION ----------------------------

        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/action") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /action\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/action/") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /action/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/action") && methodName.equals("POST")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Post with path : /action\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/action") && methodName.equals("PUT")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Put with path : /action\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/action/ticket/") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /action/ticket/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }


        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/action/") && methodName.equals("DELETE")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Delete with path : /action/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //----------------------------------CRM - TICKET_TASK ----------------------------

        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/task") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /task\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/task/") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /task/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/task") && methodName.equals("POST")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Post with path : /task\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/task") && methodName.equals("PUT")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Put with path : /task\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/task/ticket/") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /task/ticket/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/task/") && methodName.equals("DELETE")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Delete with path : /task/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //----------------------------------CRM - TICKET_AGGREGATION ----------------------------

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/ticket-aggregation/") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /ticket-aggregation/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/ticket-aggregation/user/") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /ticket-aggregation/user/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //----------------------------------CONTACT & ADDRESS ----------------------------


        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/addresses") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /addresses\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }



        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/addresses/") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /addresses/\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/contacts") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /contact\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }



        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/contacts/") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /contact/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/contacts/search/") && methodName.equals("GET")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Get with path : /contact/search/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/addresses") && methodName.equals("POST")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Post with path : /adresses\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/contacts") && methodName.equals("POST")) {
            if ((roleBDD.equals("1") || roleBDD.equals("2")) )
            {

                System.out.println("Method Post with path : /contacts\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/contacts/") && methodName.equals("PUT")) {
            String id = path.split("/")[2];
            String uuid = userService.getUuid_user();
            if (( roleBDD.equals("2") && uuid.equals(id)) ||  roleBDD.equals("1"))
            {

                System.out.println("Method Put with path : /contacts/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/addresses/") && methodName.equals("PUT")) {
            String id = path.split("/")[2];
            String uuid = userService.getUuid_user();
            if (( roleBDD.equals("2") && uuid.equals(id)) ||  roleBDD.equals("1"))
            {

                System.out.println("Method Put with path : /addresses/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/contacts/") && methodName.equals("DELETE")) {
            String id = path.split("/")[2];
            String uuid = userService.getUuid_user();
            if (( roleBDD.equals("2") && uuid.equals(id)) ||  roleBDD.equals("1"))
            {

                System.out.println("Method Delete with path : /contacts/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/addresses/") && methodName.equals("DELETE")) {
            String id = path.split("/")[2];
            String uuid = userService.getUuid_user();
            if (( roleBDD.equals("2") && uuid.equals(id)) ||  roleBDD.equals("1"))
            {

                System.out.println("Method Delete with path : /addresses/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE only")
                    .build());
            return;
        }
        //-----------------------------------------APARTMENT---------------------------------------------------------------//
        //-----------------------------------------APARTMENT - REQUEST---------------------------------------------------------------//

        //Get a feedback: admin , CE and member can do it
        else if (path.startsWith("/apartment/") && methodName.equals("GET")) {
            String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if ( roleBDD.equals("3") || roleBDD.equals("2") ||  roleBDD.equals("1"))
            {

                System.out.println("Method Get with path : /apartment/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins , CE and Member only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/apartment") && methodName.equals("GET")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if ( roleBDD.equals("3") || roleBDD.equals("2") ||  roleBDD.equals("1"))
            {

                System.out.println("Method Get with path : /apartment\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins , CE and Member only")
                    .build());
            return;
        }

        //Get a feedback: admin , CE and member can do it
        else if (path.equals("/apartment") && methodName.equals("POST")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if ( roleBDD.equals("2") ||  roleBDD.equals("1"))
            {

                System.out.println("Method Post with path : /apartment\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE  only")
                    .build());
            return;
        }

        //On peut faire un apartmentModel.getUuid(); code viri
        //Ce qui nous permettrai de comparer cette avec le notre (name), pour savoir si c'est bien le user actuellement connecté
        else if (path.startsWith("/apartment/") && methodName.equals("PUT")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if ( roleBDD.equals("2") ||  roleBDD.equals("1"))
            {

                System.out.println("Method Put with path : /apartment/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE  only")
                    .build());
            return;
        }

        //On peut faire un apartmentModel.getUuid(); code viri
        //Ce qui nous permettrai de comparer cette avec le notre (name), pour savoir si c'est bien le user actuellement connecté
        else if (path.startsWith("/apartment/") && methodName.equals("DELETE")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if ( roleBDD.equals("2") ||  roleBDD.equals("1"))
            {

                System.out.println("Method Delete with path : /apartment/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE  only")
                    .build());
            return;
        }

        //-----------------------------------------APARTMENT - AVAILABILITY---------------------------------------------------------------//



        else if (path.equals("/availability") && methodName.equals("GET")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if ( roleBDD.equals("3") || roleBDD.equals("2") ||  roleBDD.equals("1"))
            {

                System.out.println("Method Get with path : /availability\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins, CE and Member  only")
                    .build());
            return;
        }

        else if (path.startsWith("/availability/") && methodName.equals("GET")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if ( roleBDD.equals("3") || roleBDD.equals("2") ||  roleBDD.equals("1"))
            {

                System.out.println("Method Get with path : /availability/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins, CE and Member  only")
                    .build());
            return;
        }

        else if (path.equals("/availability") && methodName.equals("POST")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if ( roleBDD.equals("2") ||  roleBDD.equals("1"))
            {

                System.out.println("Method Post with path : /availability\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE  only")
                    .build());
            return;
        }

        //On peut faire un apartmentModel.getUuid(); code viri
        //Ce qui nous permettrai de comparer cette avec le notre (name), pour savoir si c'est bien le user actuellement connecté
        else if (path.startsWith("/availability/") && methodName.equals("PUT")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if ( roleBDD.equals("2") ||  roleBDD.equals("1"))
            {

                System.out.println("Method Put with path : /availability/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE  only")
                    .build());
            return;
        }

        //On peut faire un apartmentModel.getUuid(); code viri
        //Ce qui nous permettrai de comparer cette avec le notre (name), pour savoir si c'est bien le user actuellement connecté
        else if (path.startsWith("/availability/") && methodName.equals("DELETE")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if ( roleBDD.equals("2") ||  roleBDD.equals("1"))
            {

                System.out.println("Method Delete with path : /availability/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE  only")
                    .build());
            return;
        }

        //-----------------------------------------APARTMENT - OWNER---------------------------------------------------------------//

        //On peut faire un apartmentModel.getUuid(); code viri
        //Ce qui nous permettrai de comparer cette avec le notre (name), pour savoir si c'est bien le user actuellement connecté
        else if (path.equals("/owner") && methodName.equals("GET")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if ( roleBDD.equals("2") ||  roleBDD.equals("1"))
            {

                System.out.println("Method Get with path : /owner\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE  only")
                    .build());
            return;
        }

        //On peut faire un apartmentModel.getUuid(); code viri
        //Ce qui nous permettrai de comparer cette avec le notre (name), pour savoir si c'est bien le user actuellement connecté
        else if (path.startsWith("/owner/") && methodName.equals("GET")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if ( roleBDD.equals("2") ||  roleBDD.equals("1"))
            {

                System.out.println("Method Get with path : /owner/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins and CE  only")
                    .build());
            return;
        }

        else if (path.equals("/owner") && methodName.equals("POST")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if (roleBDD.equals("1"))
            {

                System.out.println("Method Post with path : /owner\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins  only")
                    .build());
            return;
        }

        //On peut faire un apartmentModel.getUuid(); code viri
        //Ce qui nous permettrai de comparer cette avec le notre (name), pour savoir si c'est bien le user actuellement connecté
        else if (path.startsWith("/owner/") && methodName.equals("PUT")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if (roleBDD.equals("1"))
            {

                System.out.println("Method Put with path : /owner/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins  only")
                    .build());
            return;
        }


        //On peut faire un apartmentModel.getUuid(); code viri
        //Ce qui nous permettrai de comparer cette avec le notre (name), pour savoir si c'est bien le user actuellement connecté
        else if (path.startsWith("/owner/") && methodName.equals("DELETE")) {
            //String id = path.split("/")[2];
            //String uuid = userService.getUuid_user();
            if (roleBDD.equals("1"))
            {

                System.out.println("Method Delete with path : /owner/{id}\n");
                return;
            }
            context.abortWith(Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Admins  only")
                    .build());
            return;
        }




        //-----------------------------------------FEEDBACK---------------------------------------------------------------//

        /*
        pour les perms membres :
        -ils peuvent tous GET et GetByID
        -Pour le POST PUT DELETE il faut verifier si il a bien fait l'activite (order).
        Pour ca dans notre bdd on a le user_id et le order_id et se sont tous les 2 des String.
        Donc t'as juste a verifier que le user à bien fait l'activité.

        Pour les admin :
        -il peuvent uniquement GET, GetByID, DELETE car ils doivent agir en tant que
        moderateur et ils ont acces a toute la bdd pour pouvoir moderer donc pas vrm de condition normalement a verifier
        */


        else if (path.startsWith("/me/mid/")) { // get id
            String id = path.split("/")[6];
            //System.out.println("id extrait : " + id);

            if (methodName.equals("GET")){
                if (roleBDD.equals("1") || roleBDD.equals("3"))
                {
                    //System.out.println("ça marche get");
                    System.out.println("Method Get with path : /membership/{id}\n id : "+ id + "\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE itself and member itself only")
                        .build());
                return;
            }
            else if(methodName.equals("PUT")){
                if (roleBDD.equals("3")) {
                    if(id.equals(name)){
                        //System.out.println("ça marche PUT");
                        System.out.println("Method Put with path : /membership/{id}\n id : "+ id + "\nAs a member\n");
                        return;
                    }

                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins and member itself only")
                        .build());
                return;
            }
            else if (methodName.equals("DELETE")){
                if (roleBDD.equals("1") || roleBDD.equals("3"))
                {
                    //System.out.println("ça marche delete");
                    System.out.println("Method Delete with path : /membership/{id}\n id : "+ id + "\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins only")
                        .build());
                return;
            }
        }

        else if (path.startsWith("/me/memory/")) { // get id
            //String id = path.split("/")[6];
            //System.out.println("id extrait : " + id);

            if (methodName.equals("POST")){
                if (roleBDD.equals("3"))
                {
                    //System.out.println("ça marche post");
                    //System.out.println("Method Post with path : /membership\nAs an Admin or a CE\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins and CE only")
                        .build());
                return;
            }
        }


        else if (path.startsWith("/fe/fid/")) { // get id
            String id = path.split("/")[6];
            //System.out.println("id extrait : " + id);

            if (methodName.equals("GET")){
                if (roleBDD.equals("1") || roleBDD.equals("3"))
                {
                    //System.out.println("ça marche get");
                    System.out.println("Method Get with path : /membership/{id}\n id : "+ id + "\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE itself and member itself only")
                        .build());
                return;
            }
            else if(methodName.equals("PUT")){
                if (roleBDD.equals("3")) {
                    if(id.equals(name)){
                        //System.out.println("ça marche PUT");
                        System.out.println("Method Put with path : /membership/{id}\n id : "+ id + "\nAs a member\n");
                        return;
                    }

                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins and member itself only")
                        .build());
                return;
            }
            else if (methodName.equals("DELETE")){
                if (roleBDD.equals("1") || roleBDD.equals("3"))
                {
                    //System.out.println("ça marche delete");
                    System.out.println("Method Delete with path : /membership/{id}\n id : "+ id + "\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins only")
                        .build());
                return;
            }
        }

        else if (path.startsWith("/fe/")) { //get
            if (methodName.equals("GET")){
                if (roleBDD.equals("1") || roleBDD.equals("3"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }
            else if (methodName.equals("POST")){
                if (roleBDD.equals("3"))
                {
                    //System.out.println("ça marche post");
                    //System.out.println("Method Post with path : /membership\nAs an Admin or a CE\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins and CE only")
                        .build());
                return;
            }
        }

        else if (path.startsWith("/me/")) { //get
            if (methodName.equals("GET")){
                if (roleBDD.equals("1") || roleBDD.equals("3"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }

        }





        //-----------------------------------------ORDER & TRANSACTION---------------------------------------------------------------//

        /*

        Pour les routes Order les users peuvent faire create, update et delete. Le CE rien et les admin tout.
        Pour les routes Transaction les users peuvent rien faire, le CE pareil et les admin tout
        */


        else if (path.startsWith("/order/")) { // get id
            //String id = path.split("/")[6];
            //System.out.println("id extrait : " + id);

            if (methodName.equals("GET")){
                if (roleBDD.equals("1"))
                {
                    //System.out.println("ça marche get");
                    //System.out.println("Method Get with path : /membership/{id}\n id : "+ id + "\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE itself and member itself only")
                        .build());
                return;
            }
            else if(methodName.equals("PUT")){
                if (roleBDD.equals("1") || roleBDD.equals("3")) {

                    //System.out.println("ça marche PUT");
                    // System.out.println("Method Put with path : /membership/{id}\n id : "+ id + "\nAs a member\n");
                    return;

                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins and member itself only")
                        .build());
                return;
            }
            else if (methodName.equals("DELETE")){
                if (roleBDD.equals("1") || roleBDD.equals("3"))
                {
                    //System.out.println("ça marche delete");
                    //System.out.println("Method Delete with path : /membership/{id}\n id : "+ id + "\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins only")
                        .build());
                return;
            }
        }

        else if (path.startsWith("/transaction/")) { // get id
            //String id = path.split("/")[6];
            //System.out.println("id extrait : " + id);

            if (methodName.equals("GET")){
                if (roleBDD.equals("1"))
                {
                    //System.out.println("ça marche get");
                    //System.out.println("Method Get with path : /membership/{id}\n id : "+ id + "\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE itself and member itself only")
                        .build());
                return;
            }
            else if(methodName.equals("PUT") || methodName.equals("DELETE")){
                if (roleBDD.equals("1")) {

                    //System.out.println("ça marche PUT");
                    // System.out.println("Method Put with path : /membership/{id}\n id : "+ id + "\nAs a member\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins and member itself only")
                        .build());
                return;
            }
        }

        else if (path.equals("/order")) { //get
            if (methodName.equals("GET")){
                if (roleBDD.equals("1"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }
            else if (methodName.equals("POST")){
                if (roleBDD.equals("1") || roleBDD.equals("3"))
                {
                    //System.out.println("ça marche post");
                    //System.out.println("Method Post with path : /membership\nAs an Admin or a CE\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins and CE only")
                        .build());
                return;
            }
        }

        else if (path.equals("/transaction")) { //get
            if (methodName.equals("GET") || methodName.equals("POST")){
                if (roleBDD.equals("1"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }
        }

        //-----------------------------------------ORDER & TRANSACTION---------------------------------------------------------------//

        /*

        Pour les routes Order les users peuvent faire create, update et delete. Le CE rien et les admin tout.
        Pour les routes Transaction les users peuvent rien faire, le CE pareil et les admin tout
        /status/products/{id}
        */


        else if (path.startsWith("/status/products/") || path.startsWith("/status/subcategories/") || path.startsWith("/status/categories")) { //get
            if (methodName.equals("GET") || methodName.equals("PUT") || methodName.equals("DELETE")){
                if (roleBDD.equals("1") || roleBDD.equals("2"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }


        }

        else if (path.equals("/status/products") || path.equals("/status/subcategories") || path.equals("/status/categories")) { //get
            if (methodName.equals("GET") || methodName.equals("POST")){
                if (roleBDD.equals("1") || roleBDD.equals("2"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }
        }



        //-----------------------------------------LEARNING & AVAILABILITIES---------------------------------------------------------------//




        else if (path.startsWith("/locations/") || path.startsWith("/formations/")) { //get
            if (methodName.equals("GET")){
                if (roleBDD.equals("1") || roleBDD.equals("2") || roleBDD.equals("3"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }

            else if (methodName.equals("PUT")){
                if (roleBDD.equals("1") || roleBDD.equals("2"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }

            else if (methodName.equals("POST")){
                if (roleBDD.equals("1") || roleBDD.equals("2"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }

            else if (methodName.equals("DELETE")){
                if (roleBDD.equals("1"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }
        }



        else if (path.startsWith("/availabilities/")) { //get
            if (methodName.equals("GET")){
                if (roleBDD.equals("1") || roleBDD.equals("2") || roleBDD.equals("3"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }

            else if (methodName.equals("PUT")){
                if (roleBDD.equals("1") || roleBDD.equals("2"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }

            else if (methodName.equals("POST")){
                if (roleBDD.equals("1") || roleBDD.equals("2"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }

            else if (methodName.equals("DELETE")){
                if (roleBDD.equals("1") || roleBDD.equals("2"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }
        }



        else if (path.equals("/availabilities") || path.equals("/status") || path.equals("/formations") || path.equals("/locations")) { //get
            if (methodName.equals("GET")){
                if (roleBDD.equals("1") || roleBDD.equals("2") || roleBDD.equals("3"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }
        }


        //-----------------------------------------PURCHASE & PROVIDERS---------------------------------------------------------------//



        else if (path.startsWith("/purchase/") || path.startsWith("/provider/") || path.startsWith("/products/")) { //get
            if (methodName.equals("GET") || methodName.equals("PUT") || methodName.equals("DELETE")){
                if (roleBDD.equals("1") || roleBDD.equals("2"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }

        }



        else if (path.equals("/purchase") || path.equals("/provider")) { //get
            if (methodName.equals("GET") || methodName.equals("POST")){
                if (roleBDD.equals("1") || roleBDD.equals("2"))
                {
                    //System.out.println("ça marche GET");
                    //System.out.println("Method Get with path : /membership\nAs an Admin\n");
                    return;
                }

                // Bloquer la requête avec un code 403 (Forbidden)
                context.abortWith(Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("Access denied: Admins, CE and users only")
                        .build());
                return;
            }
        }
    }


}
