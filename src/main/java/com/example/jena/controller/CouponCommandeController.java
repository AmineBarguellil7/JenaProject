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
@RequestMapping("/coupon")
@CrossOrigin(origins = "http://localhost:4200")
public class CouponCommandeController {
    @GetMapping("/query")
    public ResponseEntity<Object> performQuery() {
        // Define a SPARQL query to get coupons
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?codePromo\n" +
                "WHERE {\n" +
                "  ?coupon rdf:type Projet-sem:Coupon .\n" +
                "  ?coupon Projet-sem:code_promo ?codePromo .\n" +
                "}";

        // Define the Fuseki service endpoint
        String serviceEndpoint = "http://localhost:3030/ds/sparql";

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
                RDFNode codePromo = solution.get("codePromo");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("codePromo", codePromo.toString());

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