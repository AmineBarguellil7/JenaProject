

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
        public ResponseEntity<Object> performQueryByAddress() {
            // Define a SPARQL query
            String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +

                    "SELECT ?panier ?delivery_address ?city ?postal_code" +
                    "WHERE {" +
                    "  ?panier rdf:type Projet-sem:Panier ;" +
                    "          Projet-sem:delivery_address ?delivery_address ." +
                    "  ?delivery_address Projet-sem:Deliveryaddresse ?deliveryAddress ;" +
                    "                   Projet-sem:City ?city ;" +
                    "                   Projet-sem:Postal_code ?postal_code .\n" +
                    "}";

            String serviceEndpoint = "http://localhost:3030/ds/sparql"; // Remplacez par l'URL de votre dataset Fuseki

            // Execute the SPARQL query against the Fuseki server
            Query query = QueryFactory.create(queryString);
            try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
                ResultSet results = qexec.execSelect();

                // Create a List or other data structure to store the query results.
                // In this example, results are stored in a List<Object>.
                List<Object> queryResults = new ArrayList<>();

                // Iterate through the results and add them to the queryResults list
                while (results.hasNext()) {
                    QuerySolution solution = results.next();
                    RDFNode panier = solution.get("panier");
                    RDFNode deliveryAddress = solution.get("delivery_address");
                    RDFNode city = solution.get("city");
                    RDFNode postalCode = solution.get("postal_code");

                    // Add the results to the queryResults list
                    Map<String, Object> resultItem = new HashMap<>();
                    resultItem.put("panier", panier.toString());
                    resultItem.put("delivery_address", deliveryAddress.toString());
                    resultItem.put("city", city.toString());
                    resultItem.put("postal_code", postalCode.toString());
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
                            "          Projet-sem:Delivery_address ?Delivery_address ;\n" +
                            "          Projet-sem:subtotal ?subtotal ;\n" +
                            "          Projet-sem:payment_method ?payment_method .\n" +
                            "}";

            try {
                Model model = ModelFactory.createDefaultModel();
                QueryExecution queryExecution = QueryExecutionFactory.create(queryString, model);
                ResultSet resultSet = queryExecution.execSelect();
                ResultSetRewindable rewindable = ResultSetFactory.copyResults(resultSet);

                // Vous pouvez maintenant parcourir les résultats et les traiter comme vous le souhaitez
                while (rewindable.hasNext()) {
                    QuerySolution solution = rewindable.nextSolution();
                    String user_id = solution.get("user_id").toString();
                    String orders = solution.get("orders").toString();
                    String deliveryAddress = solution.get("Delivery_address").toString();
                    String subtotal = solution.get("subtotal").toString();
                    String paymentMethod = solution.get("payment_method").toString();

                    // Traitez les valeurs comme vous le souhaitez
                }

                // Fermez la QueryExecution lorsque vous avez terminé
                queryExecution.close();

                return new ResponseEntity<>("Opération réussie", HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>("Erreur lors de la récupération des paniers", HttpStatus.INTERNAL_SERVER_ERROR);
            }}


        @GetMapping("/queryByPaymentMethod")
        public ResponseEntity<Object> getCartByPaymentMethod(@RequestParam(value = "paymentMethod", required = true) String paymentMethod) {
            // Define a SPARQL query
            String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                    "SELECT ?panier ?user_id ?items ?Delivery_address ?discounts ?subtotal ?payment_method\n" +
                    "WHERE {\n" +
                    "  ?panier rdf:type Projet-sem:Panier ;\n" +
                    "          Projet-sem:user_id ?user_id ;\n" +
                    "          Projet-sem:orders ?items ;\n" +
                    "          Projet-sem:Delivery_address ?Delivery_address ;\n" +
                    "          Projet-sem:discounts ?discounts ;\n" +
                    "          Projet-sem:subtotal ?subtotal ;\n" +
                    "          Projet-sem:payment_method ?payment_method .\n" +
                    "  FILTER (str(?payment_method) = \"" + paymentMethod + "\")\n" +
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
                    String user_id = solution.get("user_id").toString();
                    String items = solution.get("items").toString();
                    String Delivery_address = solution.get("Delivery_address").toString();
                    String discounts = solution.get("discounts").toString();
                    String subtotal = solution.get("subtotal").toString();
                    String payment_method = solution.get("payment_method").toString();

                    // Add the results to the queryResults list
                    Map<String, String> resultItem = new HashMap<>();
                    resultItem.put("panier", panier);
                    resultItem.put("user_id", user_id);
                    resultItem.put("items", items);
                    resultItem.put("Delivery_address", Delivery_address);
                    resultItem.put("discounts", discounts);
                    resultItem.put("subtotal", subtotal);
                    resultItem.put("payment_method", payment_method);
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

        private final String fusekiUpdateEndpoint = "http://localhost:3030/ds/update"; // Assurez-vous que l'URL est correcte

        @PostMapping("/addCart")
        public ResponseEntity<String> addCart(@RequestBody Map<String, Object> cartData) {
            String userID = cartData.get("user_id").toString();
            String[] orders = (String[]) cartData.get("orders");
            String deliveryAddress = cartData.get("deliveryAddress").toString();
            String subtotal = cartData.get("subtotal").toString();
            String paymentMethod = cartData.get("paymentMethod").toString();

            // Créez une requête SPARQL avec des paramètres
            String queryString =
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                            "INSERT DATA {" +
                            "  Projet-sem:NouveauPanier rdf:type Projet-sem:Panier ;" +
                            "    Projet-sem:user_id \"" + userID + "\" ;" +
                            "    Projet-sem:orders \"" + String.join(",", orders) + "\" ;" +
                            "    Projet-sem:Delivery_address \"" + deliveryAddress + "\" ;" +
                            "    Projet-sem:subtotal \"" + subtotal + "\" ;" +
                            "    Projet-sem:payment_method \"" + paymentMethod + "\" ." +
                            "}";

            try {
                Model model = ModelFactory.createDefaultModel();
                UpdateAction.parseExecute(queryString, model);
                return new ResponseEntity<>("Panier ajouté avec succès", HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>("Erreur lors de l'ajout de panier", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        }





