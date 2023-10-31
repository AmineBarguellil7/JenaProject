package com.example.jena.controller;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/store")
@CrossOrigin(origins = "http://localhost:4200")
public class StoreController {
    @GetMapping("/boutiqueQuery")
    public ResponseEntity<Object> performBoutiqueQuery() {
        // Define a SPARQL query for the 'boutique' model
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?idStore ?nameStore ?description ?phoneNumber\n" +
                "WHERE {\n" +
                "  ?boutique rdf:type Projet-sem:Boutique .\n" +
                "  ?boutique Projet-sem:idStore ?idStore .\n" +
                "  ?boutique Projet-sem:nameStore ?nameStore .\n" +
                "  ?boutique Projet-sem:description ?description .\n" +
                "  ?boutique Projet-sem:phoneNumber ?phoneNumber .\n" +
                "}";

        // Define the Fuseki service endpoint
        String serviceEndpoint = "http://localhost:3030/ds/sparql"; // Replace with your dataset name

        // Execute the SPARQL query against the Fuseki server
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            // Create a List or other data structure to store the query results.
            List<Object> queryResults = new ArrayList<>();

            // Iterate through the results and add them to the queryResults list
            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode idStore = solution.get("idStore");
                RDFNode nameStore = solution.get("nameStore");
                RDFNode description = solution.get("description");
                RDFNode phoneNumber = solution.get("phoneNumber");

                // Adding the results to a Map
                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("idStore", idStore.toString());
                resultItem.put("nameStore", nameStore.toString());
                resultItem.put("description", description.toString());
                resultItem.put("phoneNumber", phoneNumber.toString());

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
    @GetMapping("/boutiqueByName")
    public ResponseEntity<Object> performBoutiqueQueryByName(String boutiqueName) {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?idStore ?nameStore ?description ?phoneNumber\n" +
                "WHERE {\n" +
                "  ?boutique rdf:type Projet-sem:Boutique .\n" +
                "  ?boutique Projet-sem:idStore ?idStore .\n" +
                "  ?boutique Projet-sem:nameStore ?nameStore .\n" +
                "  ?boutique Projet-sem:description ?description .\n" +
                "  ?boutique Projet-sem:phoneNumber ?phoneNumber .\n" +
                "  FILTER(regex(?nameStore, \"" + boutiqueName + "\", \"i\"))\n" + // Filter by boutique name
                "}";

        String serviceEndpoint = "http://localhost:3030/ds/sparql"; // Replace with your dataset name

        // Execute the SPARQL query against the Fuseki server
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            // Create a List or other data structure to store the query results.
            List<Object> queryResults = new ArrayList<>();

            // Iterate through the results and add them to the queryResults list
            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode idStore = solution.get("idStore");
                RDFNode nameStore = solution.get("nameStore");
                RDFNode description = solution.get("description");
                RDFNode phoneNumber = solution.get("phoneNumber");

                // Adding the results to a Map
                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("idStore", idStore.toString());
                resultItem.put("nameStore", nameStore.toString());
                resultItem.put("description", description.toString());
                resultItem.put("phoneNumber", phoneNumber.toString());

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
    @GetMapping("promotions")
    public ResponseEntity<Object> getPromotionWithBoutiqueInfo() {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?nameStore ?percentage ?startDate ?endDate\n" +
                "WHERE {\n" +
                "  ?boutique rdf:type Projet-sem:Boutique .\n" +
                "  ?boutique Projet-sem:nameStore ?nameStore .\n" +
                "  ?boutique Projet-sem:offre ?promotion .\n" +
                "  ?promotion rdf:type Projet-sem:Promotion .\n" +
                "  ?promotion Projet-sem:percentage ?percentage .\n" +
                "  ?promotion Projet-sem:startDate ?startDate .\n" +
                "  ?promotion Projet-sem:endDate ?endDate .\n" +
                "}";

        String serviceEndpoint = "http://localhost:3030/ds/sparql"; // Replace with your dataset name

        // Execute the SPARQL query against the Fuseki server
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();
            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode nameStore = solution.get("nameStore");
                RDFNode percentage = solution.get("percentage");
                RDFNode startDate = solution.get("startDate");
                RDFNode endDate = solution.get("endDate");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("nameStore", nameStore.toString());
                resultItem.put("percentage", percentage.toString());
                resultItem.put("startDate", startDate.toString());
                resultItem.put("endDate", endDate.toString());

                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/latestPromotion")
    public ResponseEntity<Object> getLatestPromotion() {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?nameStore ?percentage ?endDate\n" +
                "WHERE {\n" +
                "  ?boutique rdf:type Projet-sem:Boutique .\n" +
                "  ?boutique Projet-sem:nameStore ?nameStore .\n" +
                "  ?boutique Projet-sem:offre ?promotion .\n" +
                "  ?promotion rdf:type Projet-sem:Promotion .\n" +
                "  ?promotion Projet-sem:percentage ?percentage .\n" +
                "  ?promotion Projet-sem:endDate ?endDate .\n" +
                "}\n" +
                "ORDER BY DESC(?endDate)\n" +
                "LIMIT 1";

        String serviceEndpoint = "http://localhost:3030/ds/sparql"; // Replace with your dataset endpoint

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();
            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode nameStore = solution.get("nameStore");
                RDFNode percentage = solution.get("percentage");
                RDFNode endDate = solution.get("endDate");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("nameStore", nameStore.toString());
                resultItem.put("percentage", percentage.toString());
                resultItem.put("endDate", endDate.toString());

                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
