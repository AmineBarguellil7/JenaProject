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
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:4200")


public class ProductController {
    @GetMapping("/getProducts")
    public ResponseEntity<Object> sparQl() {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?id ?name ?description ?price ?quantity ?weight ?image_url \n" +
                "WHERE {\n" +
                "  ?produit rdf:type Projet-sem:Produit .\n" +
                "  ?produit Projet-sem:id ?id .\n" +
                "  ?produit Projet-sem:name ?name .\n" +
                "  ?produit Projet-sem:description ?description .\n" +
                "  ?produit Projet-sem:price ?price .\n" +
                "  ?produit Projet-sem:quantity ?quantity .\n" +
                "  ?produit Projet-sem:weight ?weight .\n" +
                "  ?produit Projet-sem:image_url ?image_url .\n"+
                "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();
            System.out.println(results);

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode id = solution.get("id");
                RDFNode name = solution.get("name");
                RDFNode description = solution.get("description");
                RDFNode price = solution.get("price");
                RDFNode quantity = solution.get("quantity");
                RDFNode weight = solution.get("weight");
                RDFNode image_url = solution.get("image_url");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("id", id.toString());
                resultItem.put("name", name.toString());
                resultItem.put("description", description.toString());
                resultItem.put("price", price.toString());
                resultItem.put("quantity", quantity.toString());
                resultItem.put("weight", weight.toString());
                resultItem.put("image_url", image_url.toString());

                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/productsByName")
    public ResponseEntity<Object> getProductsByName(@RequestParam(value = "name", required = false) String name) {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?id ?name ?description ?price ?quantity ?weight ?image_url \n" +
                "WHERE {\n" +
                "  ?product rdf:type Projet-sem:Product .\n" +
                "  ?product Projet-sem:id ?id .\n" +
                "  ?product Projet-sem:name ?name .\n" +
                "  ?product Projet-sem:description ?description .\n" +
                "  ?product Projet-sem:price ?price .\n" +
                "  ?product Projet-sem:quantity ?quantity .\n" +
                "  ?product Projet-sem:weight ?weight .\n" +
                "  ?product Projet-sem:image_url ?image_url .\n" +
                (name != null && !name.isEmpty() ? "  FILTER regex(?name, '" + name + "', 'i').\n" : "") +
                "}";

        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode id = solution.get("id");
                RDFNode productName = solution.get("name");
                RDFNode description = solution.get("description");
                RDFNode price = solution.get("price");
                RDFNode quantity = solution.get("quantity");
                RDFNode weight = solution.get("weight");
                RDFNode image_url = solution.get("image_url");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("id", id.toString());
                resultItem.put("name", productName.toString());
                resultItem.put("description", description.toString());
                resultItem.put("price", price.toString());
                resultItem.put("quantity", quantity.toString());
                resultItem.put("weight", weight.toString());
                resultItem.put("image_url", image_url.toString());

                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
