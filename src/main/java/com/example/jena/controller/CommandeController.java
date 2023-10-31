package com.example.jena.controller;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/commande")
@CrossOrigin(origins = "http://localhost:4200")
public class CommandeController {
    @GetMapping("/queryCommandeParameters")
    public ResponseEntity<Object> performCommandeParametersQuery() {
        // Define a SPARQL query to retrieve specific properties of the Commande individual
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT  ?idCommande ?deliveryAddress ?quantity ?codePromo ?totalPrice\n" +
                "WHERE {\n" +

                "  <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#commande1> rdf:type Projet-sem:Commande .\n" +
                "  <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#commande1> Projet-sem:id_commande ?idCommande .\n" +
                "  <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#commande1> Projet-sem:delivery_address ?deliveryAddress .\n" +
                "  <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#commande1> Projet-sem:quantityC ?quantity .\n" +
                "  <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#commande1> Projet-sem:code_promo ?codePromo .\n" +
                "  <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#commande1> Projet-sem:total_price ?totalPrice .\n" +
                "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql"; // Replace with your dataset name

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
                RDFNode idCommande = solution.get("idCommande");
                RDFNode deliveryAddress = solution.get("deliveryAddress");
                RDFNode quantity = solution.get("quantity");
                RDFNode codePromo = solution.get("codePromo");
                RDFNode totalPrice = solution.get("totalPrice");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("idCommande", idCommande.asLiteral().getLong()); // Convert to long
                resultItem.put("deliveryAddress", deliveryAddress.toString());
                resultItem.put("quantity", quantity.asLiteral().getInt()); // Convert to int
                resultItem.put("codePromo", codePromo.toString());
                resultItem.put("totalPrice", totalPrice.asLiteral().getFloat()); // Convert to float

                // Add the resultItem to the queryResults list
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
    @GetMapping("/getById/{id}")
    public ResponseEntity<Object> getCommandeById(@PathVariable("id") long id) {
        // Define a SPARQL query to retrieve specific properties of the Commande individual by id
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?idCommande ?deliveryAddress ?quantity ?codePromo ?totalPrice\n" +
                "WHERE {\n" +
                "  ?commande rdf:type Projet-sem:Commande .\n" +
                "  ?commande Projet-sem:id_commande ?idCommande .\n" +
                "  ?commande Projet-sem:delivery_address ?deliveryAddress .\n" +
                "  ?commande Projet-sem:quantityC ?quantity .\n" +
                "  ?commande Projet-sem:code_promo ?codePromo .\n" +
                "  ?commande Projet-sem:total_price ?totalPrice .\n" +
                "  FILTER (?idCommande = " + id + ")\n" +
                "}";

        String serviceEndpoint = "http://localhost:3030/ds/sparql"; // Replace with your dataset name

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
                RDFNode idCommande = solution.get("idCommande");
                RDFNode deliveryAddress = solution.get("deliveryAddress");
                RDFNode quantity = solution.get("quantity");
                RDFNode codePromo = solution.get("codePromo");
                RDFNode totalPrice = solution.get("totalPrice");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("idCommande", idCommande.asLiteral().getLong()); // Convert to long
                resultItem.put("deliveryAddress", deliveryAddress.toString());
                resultItem.put("quantity", quantity.asLiteral().getInt()); // Convert to int
                resultItem.put("codePromo", codePromo.toString());
                resultItem.put("totalPrice", totalPrice.asLiteral().getFloat()); // Convert to float

                // Add the resultItem to the queryResults list
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

    @GetMapping("/getByDeliveryAddress/{address}")
    public ResponseEntity<Object> getCommandeByDeliveryAddress(@PathVariable("address") String address) {
        // Define a SPARQL query to retrieve specific properties of the Commande individual by delivery address
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?idCommande ?deliveryAddress ?quantity ?codePromo ?totalPrice\n" +
                "WHERE {\n" +
                "  ?commande rdf:type Projet-sem:Commande .\n" +
                "  ?commande Projet-sem:id_commande ?idCommande .\n" +
                "  ?commande Projet-sem:delivery_address ?deliveryAddress .\n" +
                "  ?commande Projet-sem:quantityC ?quantity .\n" +
                "  ?commande Projet-sem:code_promo ?codePromo .\n" +
                "  ?commande Projet-sem:total_price ?totalPrice .\n" +
                "  FILTER (str(?deliveryAddress) = '" + address + "')\n" +
                "}";

        String serviceEndpoint = "http://localhost:3030/ds/sparql"; // Replace with your dataset name

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
                RDFNode idCommande = solution.get("idCommande");
                RDFNode deliveryAddress = solution.get("deliveryAddress");
                RDFNode quantity = solution.get("quantity");
                RDFNode codePromo = solution.get("codePromo");
                RDFNode totalPrice = solution.get("totalPrice");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("idCommande", idCommande.asLiteral().getLong()); // Convert to long
                resultItem.put("deliveryAddress", deliveryAddress.toString());
                resultItem.put("quantity", quantity.asLiteral().getInt()); // Convert to int
                resultItem.put("codePromo", codePromo.toString());
                resultItem.put("totalPrice", totalPrice.asLiteral().getFloat()); // Convert to float

                // Add the resultItem to the queryResults list
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

    @GetMapping("/getByCodePromo/{code}")
    public ResponseEntity<Object> getCommandeByCodePromo(@PathVariable("code") String code) {
        // Define a SPARQL query to retrieve specific properties of the Commande individual by code promo
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?idCommande ?deliveryAddress ?quantity ?codePromo ?totalPrice\n" +
                "WHERE {\n" +
                "  ?commande rdf:type Projet-sem:Commande .\n" +
                "  ?commande Projet-sem:id_commande ?idCommande .\n" +
                "  ?commande Projet-sem:delivery_address ?deliveryAddress .\n" +
                "  ?commande Projet-sem:quantityC ?quantity .\n" +
                "  ?commande Projet-sem:code_promo ?codePromo .\n" +
                "  ?commande Projet-sem:total_price ?totalPrice .\n" +
                "  FILTER (str(?codePromo) = '" + code + "')\n" +
                "}";

        String serviceEndpoint = "http://localhost:3030/ds/sparql"; // Replace with your dataset name

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
                RDFNode idCommande = solution.get("idCommande");
                RDFNode deliveryAddress = solution.get("deliveryAddress");
                RDFNode quantity = solution.get("quantity");
                RDFNode codePromo = solution.get("codePromo");
                RDFNode totalPrice = solution.get("totalPrice");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("idCommande", idCommande.asLiteral().getLong()); // Convert to long
                resultItem.put("deliveryAddress", deliveryAddress.toString());
                resultItem.put("quantity", quantity.asLiteral().getInt()); // Convert to int
                resultItem.put("codePromo", codePromo.toString());
                resultItem.put("totalPrice", totalPrice.asLiteral().getFloat()); // Convert to float

                // Add the resultItem to the queryResults list
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