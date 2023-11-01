package com.example.jena.controller;


import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/event")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

    @GetMapping("/query")
    public ResponseEntity<Object> performQuery() {
        // Define a SPARQL query
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?id_event ?name_event ?start_date ?end_date ?location ?entry_price\n" +
                "WHERE {\n" +
                "  ?Event rdf:type Projet-sem:Event .\n" +
                "  ?Event Projet-sem:id_event ?id_event .\n" +
                "  ?Event Projet-sem:name_event ?name_event .\n" +
                "  ?Event Projet-sem:start_date ?start_date .\n" +
                "  ?Event Projet-sem:end_date ?end_date .\n" +
                "  ?Event Projet-sem:location ?location .\n" +
                "  ?Event Projet-sem:entry_price ?entry_price .\n" +
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
                RDFNode id_event = solution.get("id_event");
                RDFNode name_event = solution.get("name_event");
                RDFNode start_date = solution.get("start_date");
                RDFNode end_date = solution.get("end_date");
                RDFNode location = solution.get("location");
                RDFNode entry_price = solution.get("entry_price");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("id_event", id_event.toString());
                resultItem.put("name_event", name_event.toString());
                resultItem.put("location", location.toString());
                resultItem.put("entry_price", Double.parseDouble(entry_price.toString().split("\\^")[0]));
                String startdate = start_date.toString();
                String enddate = end_date.toString();
                SimpleDateFormat rdfDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date sdate = rdfDateFormat.parse(startdate);
                    Date edate = rdfDateFormat.parse(enddate);
                    resultItem.put("start_date", outputDateFormat.format(sdate));
                    resultItem.put("end_date", outputDateFormat.format(edate));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                    // Handle date parsing error
                    resultItem.put("start_date", "Invalid Date");
                    resultItem.put("end_date", "Invalid Date");
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

    @GetMapping("/eventByName")
    public ResponseEntity<Object> getEventByName(@RequestParam(value = "nameevent", required = false) String nameevent) {

        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?id_event ?name_event ?start_date ?end_date ?location ?entry_price\n" +
                "WHERE {\n" +
                "  ?Event rdf:type Projet-sem:Event .\n" +
                "  ?event Projet-sem:id_event ?id_event .\n" +
                "  ?event Projet-sem:name_event ?name_event .\n" +
                "  ?event Projet-sem:start_date ?start_date .\n" +
                "  ?event Projet-sem:end_date ?end_date .\n" +
                "  ?event Projet-sem:location ?location .\n" +
                "  ?event Projet-sem:entry_price ?entry_price .\n" +
                (nameevent!= null && !nameevent.isEmpty() ? "  FILTER regex(?name_event, '" + nameevent + "', 'i').\n" : "") +
                "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql";


        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();


            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode id_event = solution.get("id_event");
                RDFNode name_event = solution.get("name_event");
                RDFNode location = solution.get("location");
                RDFNode entry_price = solution.get("entry_price");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("id_event", id_event.toString());
                resultItem.put("name_event", name_event.toString());
                resultItem.put("location", location.toString());
                resultItem.put("entry_price", Double.parseDouble(entry_price.toString().split("\\^")[0]));

                queryResults.add(resultItem);


            }


            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/eventByLocation")
    public ResponseEntity<Object> getEventByLocation(@RequestParam(value = "location", required = true) String locationn) {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?id_event ?name_event ?start_date ?end_date ?location ?entry_price\n" +
                "WHERE {\n" +
                "  ?Event rdf:type Projet-sem:Event .\n" +
                "  ?event Projet-sem:id_event ?id_event .\n" +
                "  ?event Projet-sem:name_event ?name_event .\n" +
                "  ?event Projet-sem:start_date ?start_date .\n" +
                "  ?event Projet-sem:end_date ?end_date .\n" +
                "  ?event Projet-sem:location ?location .\n" +
                "  ?event Projet-sem:entry_price ?entry_price .\n" +
                (locationn != null && !locationn.isEmpty() ? "  ?location Projet-sem:nom '" + locationn + "'.\n" : "") +
                "}";


        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode id_event = solution.get("id_event");
                RDFNode name_event = solution.get("name_event");
                RDFNode start_date = solution.get("start_date");
                RDFNode end_date = solution.get("end_date");
                RDFNode location = solution.get("location");
                RDFNode entry_price = solution.get("entry_price");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("id_event", id_event.toString());
                resultItem.put("name_event", name_event.toString());
                resultItem.put("location", location.toString());
                resultItem.put("entry_price", Double.parseDouble(entry_price.toString().split("\\^")[0]));
                String startdate = start_date.toString();
                String enddate = end_date.toString();
                SimpleDateFormat rdfDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date sdate = rdfDateFormat.parse(startdate);
                    Date edate = rdfDateFormat.parse(enddate);
                    resultItem.put("start_date", outputDateFormat.format(sdate));
                    resultItem.put("end_date", outputDateFormat.format(edate));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                    // Handle date parsing error
                    resultItem.put("start_date", "Invalid Date");
                    resultItem.put("end_date", "Invalid Date");
                }
                // Add the resultItem to the queryResults list
                queryResults.add(resultItem);
            }

            return new ResponseEntity<>(queryResults, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/eventByStartDate")
    public ResponseEntity<Object> getEventByStartDate(@RequestParam(value = "filter", required = false) String filter) {


        String orderByClause = "";

        // Determine the sorting order based on the filter parameter
        if (filter != null && filter.equalsIgnoreCase("recent")) {
            orderByClause = "DESC(?start_date)";
        } else if (filter != null && filter.equalsIgnoreCase("oldest")) {
            orderByClause = "ASC(?start_date)";
        }



        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX Projet-sem: <http://www.semanticweb.org/aminebarguellil/ontologies/2023/9/Projet-sem#>\n" +
                "SELECT ?id ?titreArticle ?contenu ?auteur ?datePublication\n" +
                "WHERE {\n" +
                "  ?event rdf:type Projet-sem:Event .\n" +
                "  ?event Projet-sem:id_event ?id_event .\n" +
                "  ?event Projet-sem:name_event ?name_event .\n" +
                "  ?event Projet-sem:start_date ?start_date .\n" +
                "  ?event Projet-sem:end_date ?end_date .\n" +
                "  ?event Projet-sem:location ?location .\n" +
                "  ?event Projet-sem:entry_price ?entry_price .\n" +
                "}\n" +
                (orderByClause.isEmpty() ? "" : "ORDER BY " + orderByClause);


        String serviceEndpoint = "http://localhost:3030/ds/sparql";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            ResultSet results = qexec.execSelect();

            List<Object> queryResults = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                RDFNode id_event = solution.get("id_event");
                RDFNode name_event = solution.get("name_event");
                RDFNode start_date = solution.get("start_date");
                RDFNode end_date = solution.get("end_date");
                RDFNode location = solution.get("location");
                RDFNode entry_price = solution.get("entry_price");

                Map<String, Object> resultItem = new HashMap<>();
                resultItem.put("id_event", id_event.toString());
                resultItem.put("name_event", name_event.toString());
                resultItem.put("location", location.toString());
                resultItem.put("entry_price", Double.parseDouble(entry_price.toString().split("\\^")[0]));
                String startdate = start_date.toString();
                String enddate = end_date.toString();
                SimpleDateFormat rdfDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date sdate = rdfDateFormat.parse(startdate);
                    Date edate = rdfDateFormat.parse(enddate);
                    resultItem.put("start_date", outputDateFormat.format(sdate));
                    resultItem.put("end_date", outputDateFormat.format(edate));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                    // Handle date parsing error
                    resultItem.put("start_date", "Invalid Date");
                    resultItem.put("end_date", "Invalid Date");
                }
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
