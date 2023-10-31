package com.example.jena.controller;


import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/article")
@CrossOrigin(origins = "http://localhost:4200")
public class ArticleController {
    @GetMapping("/query")
    public ResponseEntity<Object> performQuery() {
        // Define a SPARQL query
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?id ?titreArticle ?contenu ?auteur ?datePublication\n" +
                "WHERE {\n" +
                "  ?article rdf:type Projet-sem:Article .\n" +
                "  ?article Projet-sem:id ?id .\n" +
                "  ?article Projet-sem:titreArticle ?titreArticle .\n" +
                "  ?article Projet-sem:contenu ?contenu .\n" +
                "  ?article Projet-sem:auteur ?auteur .\n" +
                "  ?article Projet-sem:datePublication ?datePublication .\n" +
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
                RDFNode id = solution.get("id");
                RDFNode titreArticle = solution.get("titreArticle");
                RDFNode contenu = solution.get("contenu");
                RDFNode auteur = solution.get("auteur");
                RDFNode datePublication = solution.get("datePublication");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("id", id.toString());
                resultItem.put("titreArticle", titreArticle.toString());
                resultItem.put("contenu", contenu.toString());
                resultItem.put("auteur", auteur.toString());
                String datePublicationStr = datePublication.toString();
                SimpleDateFormat rdfDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = rdfDateFormat.parse(datePublicationStr);
                    resultItem.put("datePublication", outputDateFormat.format(date));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                    // Handle date parsing error
                    resultItem.put("datePublication", "Invalid Date");
                }

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

    @GetMapping("/articlesByTitre")
    public ResponseEntity<Object> getArticlesByTitre(@RequestParam(value = "titre", required = false) String titre) {

        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?id ?titreArticle ?contenu ?auteur\n" +
                "WHERE {\n" +
                "  ?article rdf:type Projet-sem:Article .\n" +
                "  ?article Projet-sem:id ?id .\n" +
                "  ?article Projet-sem:titreArticle ?titreArticle .\n" +
                "  ?article Projet-sem:contenu ?contenu .\n" +
                "  ?article Projet-sem:auteur ?auteur .\n" +
                (titre != null && !titre.isEmpty() ? "  FILTER regex(?titreArticle, '" + titre + "', 'i').\n" : "") +
                "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql";


        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();


            List<Object> queryResults = new ArrayList<>();



            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode id = solution.get("id");
                RDFNode titreArticle = solution.get("titreArticle");
                RDFNode contenu = solution.get("contenu");
                RDFNode auteur = solution.get("auteur");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("id", id.toString());
                resultItem.put("titreArticle", titreArticle.toString());
                resultItem.put("contenu", contenu.toString());
                resultItem.put("auteur", auteur.toString());


                queryResults.add(resultItem);


            }


            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/articlesByCategory")
    public ResponseEntity<Object> getArticlesByCategory(@RequestParam(value = "category", required = true) String category) {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?id ?titreArticle ?contenu ?auteur\n" +
                "WHERE {\n" +
                "  ?article rdf:type Projet-sem:Article .\n" +
                "  ?article Projet-sem:id ?id .\n" +
                "  ?article Projet-sem:titreArticle ?titreArticle .\n" +
                "  ?article Projet-sem:contenu ?contenu .\n" +
                "  ?article Projet-sem:auteur ?auteur .\n" +
                "  ?article Projet-sem:decrit ?categoryUri .\n" + // Utiliser "Projet-sem:decrit" pour lier la catégorie
                (category != null && !category.isEmpty() ? "  ?categoryUri Projet-sem:nom '" + category + "'.\n" : "") +
                "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode id = solution.get("id");
                RDFNode titreArticle = solution.get("titreArticle");
                RDFNode contenu = solution.get("contenu");
                RDFNode auteur = solution.get("auteur");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("id", id.toString());
                resultItem.put("titreArticle", titreArticle.toString());
                resultItem.put("contenu", contenu.toString());
                resultItem.put("auteur", auteur.toString());

                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/articlesByDatePublication")
    public ResponseEntity<Object> getArticlesByDatePublication(@RequestParam(value = "filter", required = false) String filter) {


        String orderByClause = "";

        // Determine the sorting order based on the filter parameter
        if (filter != null && filter.equalsIgnoreCase("recent")) {
            orderByClause = "DESC(?datePublication)";
        } else if (filter != null && filter.equalsIgnoreCase("oldest")) {
            orderByClause = "ASC(?datePublication)";
        }



        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?id ?titreArticle ?contenu ?auteur ?datePublication\n" +
                "WHERE {\n" +
                "  ?article rdf:type Projet-sem:Article .\n" +
                "  ?article Projet-sem:id ?id .\n" +
                "  ?article Projet-sem:titreArticle ?titreArticle .\n" +
                "  ?article Projet-sem:contenu ?contenu .\n" +
                "  ?article Projet-sem:auteur ?auteur .\n" +
                "  ?article Projet-sem:datePublication ?datePublication .\n" +
                "}\n" +
                (orderByClause.isEmpty() ? "" : "ORDER BY " + orderByClause);


        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode id = solution.get("id");
                RDFNode titreArticle = solution.get("titreArticle");
                RDFNode contenu = solution.get("contenu");
                RDFNode auteur = solution.get("auteur");
                RDFNode datePublication = solution.get("datePublication");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("id", id.toString());
                resultItem.put("titreArticle", titreArticle.toString());
                resultItem.put("contenu", contenu.toString());
                resultItem.put("auteur", auteur.toString());
                String datePublicationStr = datePublication.toString();
                SimpleDateFormat rdfDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = rdfDateFormat.parse(datePublicationStr);
                    resultItem.put("datePublication", outputDateFormat.format(date));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                    // Handle date parsing error
                    resultItem.put("datePublication", "Invalid Date");
                }

                queryResults.add(resultItem);

                System.out.println(queryResults);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
