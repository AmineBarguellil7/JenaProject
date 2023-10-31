package com.example.jena.controller;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.update.UpdateAction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

    @RestController
    @RequestMapping("/cart")
    @CrossOrigin(origins = "http://localhost:4200")
    public class CartController {
        @GetMapping("/cartByAddress")
        public ResponseEntity<Object> performQueryByAddress(
               @RequestParam(value = "adresseLivraison", required = true) String adresseLivraison)


               {
            // Define a SPARQL query
                   String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                           "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                           "SELECT ?user_id ?orders ?deliveryAddress ?subtotal ?payment_method\n" +
                           "WHERE {" +
                           "  ?panier rdf:type Projet-sem:Panier ;" +
                           "          Projet-sem:user_id ?user_id ;" +
                           "          Projet-sem:orders ?orders ;" +
                           "          Projet-sem:delivery_address ?deliveryAddress ;" +
                           "          Projet-sem:subtotal ?subtotal ;" +
                           "          Projet-sem:Payment_method ?payment_method ." +
                           "  ?panier Projet-sem:decrit ?adresseLivraisonUri ." +
                           "  ?adresseLivraisonUri Projet-sem:City '" + adresseLivraison + "'.\n"  +
                           "}";

                   String serviceEndpoint = "http://localhost:3030/ds/sparql"; // Remplacez par l'URL de votre dataset Fuseki

                   // Execute the SPARQL query against the Fuseki server
                   Query query = QueryFactory.create(queryString);
                   try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
                       ResultSet results = qexec.execSelect();

                       // Create a List or other data structure to store the query results.
                       List<Map<String, String>> queryResults = new ArrayList<>();

                       // Iterate through the results and add them to the queryResults list
                       while (results.hasNext()) {
                           QuerySolution solution = results.next();
                           RDFNode user_id = solution.get("user_id");
                           RDFNode orders = solution.get("orders");
                           RDFNode deliveryAddress = solution.get("deliveryAddress");
                           RDFNode subtotal = solution.get("subtotal");
                           RDFNode payment_method = solution.get("payment_method");

                           // Add the results to the queryResults list
                           Map<String, String> resultItem = new HashMap<>();
                           resultItem.put("user_id", user_id.toString());
                           resultItem.put("orders", orders.toString());
                           resultItem.put("deliveryAddress", deliveryAddress.toString());
                           resultItem.put("subtotal", subtotal.toString());
                           resultItem.put("payment_method", payment_method.toString());
                           queryResults.add(resultItem);
                       }

                       // Return the results as JSON
                       return new ResponseEntity<>(queryResults, HttpStatus.OK);
                   } catch (Exception e) {
                       e.printStackTrace();
                       // Handle any exceptions and return an appropriate HTTP response.
                       return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
                   }
               }









        @GetMapping("/getPaniers")
        public ResponseEntity<String> getPaniers() {
            String queryString =
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                            "SELECT ?user_id ?orders ?Delivery_address ?subtotal ?payment_method\n" +
                            "WHERE {\n" +
                            "  ?panier rdf:type Projet-sem:Panier ;\n" +
                            "          Projet-sem:user_id ?user_id ;\n" +
                            "          Projet-sem:orders ?orders ;\n" +
                            "          Projet-sem:delivery_address ?Delivery_address ;\n" +
                            "          Projet-sem:subtotal ?subtotal ;\n" +
                            "          Projet-sem:Payment_method ?payment_method .\n" +
                            "}";

            try {
                String serviceEndpoint = "http://localhost:3030/ds/sparql"; // Remplacez par l'URL de votre dataset Fuseki
                Query query = QueryFactory.create(queryString);
                QueryExecution queryExecution = QueryExecutionFactory.sparqlService(serviceEndpoint, query);
                ResultSet resultSet = queryExecution.execSelect();

                // Créez une structure de données (par exemple, une liste ou un objet) pour stocker les résultats
                List<Map<String, String>> panierList = new ArrayList<>();

                while (resultSet.hasNext()) {
                    QuerySolution solution = resultSet.nextSolution();
                    String user_id = solution.get("user_id").toString();
                    String orders = solution.get("orders").toString();
                    String deliveryAddress = solution.get("Delivery_address").toString();
                    String subtotal = solution.get("subtotal").toString();
                    String paymentMethod = solution.get("payment_method").toString();

                    // Ajoutez les résultats à votre structure de données
                    Map<String, String> panier = new HashMap<>();
                    panier.put("user_id", user_id);
                    panier.put("orders", orders);
                    panier.put("Delivery_address", deliveryAddress);
                    panier.put("subtotal", subtotal);
                    panier.put("payment_method", paymentMethod);
                    panierList.add(panier);
                }

                // Traitez les résultats comme vous le souhaitez

                // Fermez la QueryExecution lorsque vous avez terminé
                queryExecution.close();

                return new ResponseEntity<>("Opération réussie", HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>("Erreur lors de la récupération des paniers", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }



        @GetMapping("/queryByPaymentMethod")
        public ResponseEntity<Object> getCartByPaymentMethod(@RequestParam(value = "paymentMethod", required = true) String paymentMethod) {
            // Define a SPARQL query
            String queryString =
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                            "SELECT ?panier ?userID ?orders ?deliveryAddress ?subtotal ?paymentMethod\n" +
                            "WHERE {\n" +
                            "  ?panier rdf:type Projet-sem:Panier .\n" +
                            "  ?panier Projet-sem:user_id ?userID .\n" +
                            "  ?panier Projet-sem:orders ?orders .\n" +
                            "  ?panier Projet-sem:delivery_address ?deliveryAddress .\n" +
                            "  ?panier Projet-sem:subtotal ?subtotal .\n" +
                            "  ?panier Projet-sem:Payment_method ?paymentMethod .\n" +
                            "  FILTER (str(?paymentMethod) = '" + paymentMethod + "')\n" +
                            "}";

            String serviceEndpoint = "http://localhost:3030/ds/sparql"; // Remplacez par l'URL de votre dataset Fuseki

            // Execute the SPARQL query against the Fuseki server
            Query query = QueryFactory.create(queryString);
            try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
                ResultSet results = qexec.execSelect();

                // Create a List or other data structure to store the query results.
                List<Map<String, String>> queryResults = new ArrayList<>();

                // Iterate through the results and add them to the queryResults list
                while (results.hasNext()) {
                    QuerySolution solution = results.next();
                    String panier = solution.get("panier").toString();
                    String user_id = solution.get("userID").toString();
                    String orders = solution.get("orders").toString();
                    String deliveryAddress = solution.get("deliveryAddress").toString();
                    String subtotal = solution.get("subtotal").toString();
                    String payment_method = solution.get("paymentMethod").toString();

                    // Add the results to the queryResults list
                    Map<String, String> resultItem = new HashMap<>();
                    resultItem.put("panier", panier);
                    resultItem.put("user_id", user_id);
                    resultItem.put("orders", orders);
                    resultItem.put("deliveryAddress", deliveryAddress);
                    resultItem.put("subtotal", subtotal);
                    resultItem.put("paymentMethod", payment_method);
                    queryResults.add(resultItem);
                }

                // Return the results as JSON
                return new ResponseEntity<>(queryResults, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle any exceptions and return an appropriate HTTP response.
                return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }




    }





