package com.example.jena.controller;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/stand")
@CrossOrigin(origins = "http://localhost:4200")
public class StandController {

    @GetMapping("/query")
    public ResponseEntity<Object> performQuery() {
        // Define a SPARQL query
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?id_stand ?numero ?emplacement ?tarif_de_location ?status\n" +
                "WHERE {\n" +
                "  ?stand rdf:type Projet-sem:Stand .\n" +
                "  ?stand Projet-sem:id_stand ?id_stand .\n" +
                "  ?stand Projet-sem:numero ?numero .\n" +
                "  ?stand Projet-sem:emplacement ?emplacement .\n" +
                "  ?stand Projet-sem:tarif_de_location ?tarif_de_location .\n" +
                "  ?stand Projet-sem:status ?status .\n" +
                "}";

        // Define the Fuseki service endpoint
        String serviceEndpoint = "http://localhost:3030/ds/sparql"; // Replace with your dataset name

        // Execute the SPARQL query against the Fuseki server
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            System.out.println(results);

            // Create a List or other data structure to store the query results.
            // In this example, results are stored in a List<Object>.
            List<Object> queryResults = new ArrayList<>();

            // Iterate through the results and add them to the queryResults list
            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode id_stand = solution.get("id_stand");
                RDFNode numero = solution.get("numero");
                RDFNode emplacement = solution.get("emplacement");
                RDFNode tarif_de_location = solution.get("tarif_de_location");
                RDFNode status = solution.get("status");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("id_stand", id_stand.toString());
                resultItem.put("numero", numero.toString());
                resultItem.put("emplacement", emplacement.toString());
                resultItem.put("tarif_de_location", Double.parseDouble(tarif_de_location.toString().split("\\^")[0]));
                resultItem.put("status", status.toString());
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

    @GetMapping("/standByStatus")
    public ResponseEntity<Object> getStandByStatus(@RequestParam(value = "SearchedStatus", required = false) String SearchedStatus) {

        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?id_stand ?numero ?emplacement ?tarif_de_location ?status\n" +
                "WHERE {\n" +
                "  ?stand rdf:type Projet-sem:Stand .\n" +
                "  ?stand Projet-sem:id_stand ?id_stand .\n" +
                "  ?stand Projet-sem:numero ?numero .\n" +
                "  ?stand Projet-sem:emplacement ?emplacement .\n" +
                "  ?stand Projet-sem:tarif_de_location ?tarif_de_location .\n" +
                "  ?stand Projet-sem:status ?status .\n" +
                (SearchedStatus!= null && !SearchedStatus.isEmpty() ? "  FILTER regex(?status, '" + SearchedStatus + "', 'i').\n" : "") +
                "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql";


        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();


            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode id_stand = solution.get("id_stand");
                RDFNode numero = solution.get("numero");
                RDFNode emplacement = solution.get("emplacement");
                RDFNode tarif_de_location = solution.get("tarif_de_location");
                RDFNode status = solution.get("status");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("id_stand", id_stand.toString());
                resultItem.put("numero", numero.toString());
                resultItem.put("emplacement", emplacement.toString());
                resultItem.put("tarif_de_location", Double.parseDouble(tarif_de_location.toString().split("\\^")[0]));
                resultItem.put("status", status.toString());
                // Add the resultItem to the queryResults list
                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
