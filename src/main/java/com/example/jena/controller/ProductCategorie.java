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
@RequestMapping("/productca")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductCategorie {

    @GetMapping("/getProductCategories")
    public ResponseEntity<Object> getProductCategories() {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?nameCategorie ?DescriptionCategorie\n" +
                "WHERE {\n" +
                "  ?categorie rdf:type Projet-sem:CategorieProduit .\n" +
                "  ?categorie Projet-sem:nameCategorie ?nameCategorie .\n" +
                "  ?categorie Projet-sem:DescriptionCategorie ?DescriptionCategorie .\n" +
                "}";

        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode nameCategorie = solution.get("nameCategorie");
                RDFNode DescriptionCategorie = solution.get("DescriptionCategorie");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("nameCategorie", nameCategorie.toString());
                resultItem.put("DescriptionCategorie", DescriptionCategorie.toString());

                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/categoryByName")
    public ResponseEntity<Object> getCategoryByName(@RequestParam(value = "nameparam", required = false) String nameparam) {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?nameCategorie ?DescriptionCategorie\n" +
                "WHERE {\n" +
                "  ?categorie rdf:type Projet-sem:CategorieProduit .\n" +
                "  ?categorie Projet-sem:nameCategorie ?nameCategorie .\n" +
                "  ?categorie Projet-sem:DescriptionCategorie ?DescriptionCategorie .\n" +
                "  FILTER (?name = \"" + nameparam + "\")\n" +
                "}";

        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode nameCategorie = solution.get("nameCategorie");
                RDFNode DescriptionCategorie = solution.get("DescriptionCategorie");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("nameCategorie", nameCategorie.toString());
                resultItem.put("DescriptionCategorie", DescriptionCategorie.toString());

                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
