//package com.zegasoftware.stock_management.abac;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
//import jakarta.annotation.PostConstruct;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.List;
//
//
//
//@Service
//public class PolicyLoader {
//    private List<Policy> policies;
//    @PostConstruct
//    public void load() throws IOException {
//        ObjectMapper yaml = new ObjectMapper(new YAMLFactory());
//        JsonNode root = yaml.readTree(
//                new ClassPathResource("policies.yml").getInputStream()
//        );
//        policies = yaml.readerFor(
//                new TypeReference<List<Policy>>() {}
//        ).readValue(root.get("policies"));
//    }
//    public List<Policy> getPolicies() { return policies; }
//}
