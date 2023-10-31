package com.example.jena.controller;


import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.update.UpdateAction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;



@RestController
@RequestMapping("/article")
@CrossOrigin(origins = "http://localhost:4200")
public class ArticleController {
    @GetMapping("/query")
    public ResponseEntity<Object> performQuery() {
        // Define a SPARQL query
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?id ?titreArticle ?contenu ?auteur\n" +
                "WHERE {\n" +
                "  ?article rdf:type Projet-sem:Article .\n" +
                "  ?article Projet-sem:id ?id .\n" +
                "  ?article Projet-sem:titreArticle ?titreArticle .\n" +
                "  ?article Projet-sem:contenu ?contenu .\n" +
                "  ?article Projet-sem:auteur ?auteur .\n" +
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

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("id", id.toString());
                resultItem.put("titreArticle", titreArticle.toString());
                resultItem.put("contenu", contenu.toString());
                resultItem.put("auteur", auteur.toString());

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


    private final String fusekiUpdateEndpoint = "http://localhost:3030/ds/";
    @PostMapping("/addArticle")
    public ResponseEntity<String> addArticle(@RequestBody Map<String, Object> articleData) {
        try {
            // Récupérez les données de l'article à partir de la Map
            String titreArticle = articleData.get("titreArticle").toString();
            String contenu = articleData.get("contenu").toString();
            String auteur = articleData.get("auteur").toString();

            // Effectuez la requête SPARQL d'insertion pour ajouter l'article à votre triplestore
            String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                    "INSERT DATA {\n" +
                    "  _:article rdf:type Projet-sem:Article .\n" +
                    "  _:article Projet-sem:titreArticle '" + titreArticle + "' .\n" +
                    "  _:article Projet-sem:contenu '" + contenu + "' .\n" +
                    "  _:article Projet-sem:auteur '" + auteur + "' .\n" +
                    "}";

            // Utilisez DatasetAccessor pour accéder à la base de données RDF
            DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(fusekiUpdateEndpoint);

            // Chargez le modèle actuel depuis la base de données
            Model model = accessor.getModel();

            // Exécutez la requête SPARQL d'insertion en utilisant UpdateAction
            UpdateAction.parseExecute(queryString, model);

            // Si l'insertion a réussi, vous pouvez renvoyer une réponse appropriée
            return new ResponseEntity<>("Article ajouté avec succès", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors de l'ajout de l'article", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
