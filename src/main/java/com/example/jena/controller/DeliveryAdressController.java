package com.example.jena.controller;


import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/DeliveryAdress")
@CrossOrigin(origins = "http://localhost:4200")
public class DeliveryAdressController {
    @GetMapping("/getAddresses")
    public ResponseEntity<Object> getDeliveryAddresses() {
        // Define a SPARQL query
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n"  +
                "SELECT ?deliveryAddress ?city ?postalCode\n" +
                "WHERE {\n" +
                "  ?deliveryAddress rdf:type Projet-sem:DeliveryAddress .\n" +
                "  ?deliveryAddress Projet-sem:City ?city .\n" +
                "  ?deliveryAddress Projet-sem:PostalCode ?postalCode .\n" +
                "}";

        // Reste de votre code pour exécuter la requête SPARQL et récupérer les données.



    // Define the Fuseki service endpoint
        String serviceEndpoint = "http://localhost:3030/dataset/sparql"; // Remplacez par l'URL de votre endpoint Fuseki

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
                RDFNode deliveryAddress = solution.get("deliveryAddress");
                RDFNode city = solution.get("city");
                RDFNode postalCode = solution.get("postalCode");

                // Add the results to the queryResults list
                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("deliveryAddress", deliveryAddress.toString());
                resultItem.put("city", city.toString());
                resultItem.put("postalCode", postalCode.toString());
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
@PostMapping("/addAddress")
    public ResponseEntity<String> addAddress(@RequestBody Map<String, Object> addressData) {
        String deliveryAddress = addressData.get("DeliveryAddress").toString();
        String city = addressData.get("City").toString();
        String postalCode = addressData.get("PostalCode").toString();

        // Create a SPARQL query with parameters
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "INSERT DATA {" +
                "  _:address rdf:type Projet-sem:DeliveryAddress ;" +
                "    Projet-sem:DeliveryAddress \"" + deliveryAddress + "\" ;" +
                "    Projet-sem:City \"" + city + "\" ;" +
                "    Projet-sem:PostalCode \"" + postalCode + "\" ." +
                "}";

        try {
            Model model = ModelFactory.createDefaultModel();
            UpdateAction.parseExecute(queryString, model);
            return new ResponseEntity<>("Adresse ajoutée avec succès", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors de l'ajout de l'adresse", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }}
