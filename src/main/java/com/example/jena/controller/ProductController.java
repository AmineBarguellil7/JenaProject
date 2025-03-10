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
                "SELECT ?idProduct ?name ?description ?price ?quantity ?weight  \n" +
                "WHERE {\n" +
                "  ?produit rdf:type Projet-sem:Produit .\n" +
                "  ?produit Projet-sem:idProduct ?idProduct .\n" +
                "  ?produit Projet-sem:name ?name .\n" +
                "  ?produit Projet-sem:description ?description .\n" +
                "  ?produit Projet-sem:price ?price .\n" +
                "  ?produit Projet-sem:quantity ?quantity .\n" +
                "  ?produit Projet-sem:weight ?weight .\n" +
                "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();
            System.out.println(results);

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode idProduct = solution.get("idProduct");
                RDFNode name = solution.get("name");
                RDFNode description = solution.get("description");
                RDFNode price = solution.get("price");
                RDFNode quantity = solution.get("quantity");
                RDFNode weight = solution.get("weight");


                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("idProduct", idProduct.toString());
                resultItem.put("name", name.toString());
                resultItem.put("description", description.toString());
                resultItem.put("price", Double.parseDouble(price.toString().split("\\^")[0]));
                resultItem.put("quantity", Double.parseDouble(quantity.toString().split("\\^")[0]));
                resultItem.put("weight", Double.parseDouble(weight.toString().split("\\^")[0]));


                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/productsByName")
    public ResponseEntity<Object> getProductsByName(@RequestParam(value = "nameparam", required = false) String nameparam) {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?idProduct ?name ?description ?price ?quantity ?weight\n" +
                "WHERE {\n" +
                "  ?produit rdf:type Projet-sem:Produit .\n" +
                "  ?produit Projet-sem:idProduct ?idProduct .\n" +
                "  ?produit Projet-sem:name ?name .\n" +
                "  ?produit Projet-sem:description ?description .\n" +
                "  ?produit Projet-sem:price ?price .\n" +
                "  ?produit Projet-sem:quantity ?quantity .\n" +
                "  ?produit Projet-sem:weight ?weight .\n" +
                "  FILTER (?name = \"" + nameparam + "\")\n" +
                "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode idProduct = solution.get("idProduct");
                RDFNode nameResult = solution.get("name");
                RDFNode description = solution.get("description");
                RDFNode price = solution.get("price");
                RDFNode quantity = solution.get("quantity");
                RDFNode weight = solution.get("weight");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("idProduct", idProduct.toString());
                resultItem.put("name", nameResult.toString());
                resultItem.put("description", description.toString());
                resultItem.put("price", Double.parseDouble(price.toString().split("\\^")[0]));
                resultItem.put("quantity", Double.parseDouble(quantity.toString().split("\\^")[0]));
                resultItem.put("weight", Double.parseDouble(weight.toString().split("\\^")[0]));

                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/productsByPrice")
    public ResponseEntity<Object> getProductsByPrice(
            @RequestParam(value = "minPrice", required = false) Double minPrice
    ) {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?idProduct ?name ?description ?price ?quantity ?weight\n" +
                "WHERE {\n" +
                "  ?produit rdf:type Projet-sem:Produit .\n" +
                "  ?produit Projet-sem:idProduct ?idProduct .\n" +
                "  ?produit Projet-sem:name ?name .\n" +
                "  ?produit Projet-sem:description ?description .\n" +
                "  ?produit Projet-sem:price ?price .\n" +
                "  ?produit Projet-sem:quantity ?quantity .\n" +
                "  ?produit Projet-sem:weight ?weight .\n" +
                (minPrice != null ? "  FILTER (SUBSTR(STR(?price), 1, 2) = '" + minPrice.toString().substring(0, 2) + "').\n" : "") +
                "}";

        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode idProduct = solution.get("idProduct");
                RDFNode nameResult = solution.get("name");
                RDFNode description = solution.get("description");
                RDFNode price = solution.get("price");
                RDFNode quantity = solution.get("quantity");
                RDFNode weight = solution.get("weight");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("idProduct", idProduct.toString());
                resultItem.put("name", nameResult.toString());
                resultItem.put("description", description.toString());
                resultItem.put("price", Double.parseDouble(price.toString().split("\\^")[0]));
                resultItem.put("quantity", Double.parseDouble(quantity.toString().split("\\^")[0]));
                resultItem.put("weight", Double.parseDouble(weight.toString().split("\\^")[0]));

                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/productsByCategory")
    public ResponseEntity<Object> getProductsByCategory(@RequestParam(value = "category", required = false) String category) {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?idProduct ?name ?description ?price ?quantity ?weight\n" +
                "WHERE {\n" +
                "  ?produit rdf:type Projet-sem:Produit .\n" +
                "  ?produit Projet-sem:idProduct ?idProduct .\n" +
                "  ?produit Projet-sem:name ?name .\n" +
                "  ?produit Projet-sem:description ?description .\n" +
                "  ?produit Projet-sem:price ?price .\n" +
                "  ?produit Projet-sem:quantity ?quantity .\n" +
                "  ?produit Projet-sem:weight ?weight .\n";
        if (category != null && !category.isEmpty()) {
            queryString += "  ?produit Projet-sem:Possede ?categoryUri .\n";
            queryString += "  ?categoryUri Projet-sem:nameCategorie '" + category + "'.\n";
        }

        queryString += "}";

        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode idProduct = solution.get("idProduct");
                RDFNode nameResult = solution.get("name");
                RDFNode description = solution.get("description");
                RDFNode price = solution.get("price");
                RDFNode quantity = solution.get("quantity");
                RDFNode weight = solution.get("weight");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("idProduct", idProduct.toString());
                resultItem.put("name", nameResult.toString());
                resultItem.put("description", description.toString());
                resultItem.put("price", Double.parseDouble(price.toString().split("\\^")[0]));
                resultItem.put("quantity", Double.parseDouble(quantity.toString().split("\\^")[0]));
                resultItem.put("weight", Double.parseDouble(weight.toString().split("\\^")[0]));

                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
