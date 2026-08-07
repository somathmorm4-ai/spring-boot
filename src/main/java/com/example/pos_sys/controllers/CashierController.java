package com.example.pos_sys.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/*
end point 
1. rest API: difference end point (ex: /create, /get, /update, /delete)
2. restfull: same endpoint (ex: /cashier) but different HTTP method (GET, POST, PUT, DELETE)
*/


@Tag(name = "Cashier API")
@RestController
@RequestMapping("/api/cashier")





 
public class CashierController {

    public final JdbcTemplate jdbcTemplate;
    public CashierController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping
    public Map<String,Object> create(@RequestBody Map<String,Object> body) {
        
        // Get data from data of frontend
        Object f = body.get("fullname");
        Object p = body.get("phone");
        Object u = body.get("username");

        String sql = "INSERT INTO tb_cashiers (fullname, phone, username) VALUES (?,?,?)";
        jdbcTemplate.update(sql,f,p,u);

        return Map.of("Message", "Create Success");

    
    }


   @GetMapping ("/cashier")
   public Map<String, Object> getAll(){
    
    String sql = "SELECT * FROM tb_cashiers";

    List<Map<String, Object>> cashiers = jdbcTemplate.queryForList(sql);

    Map<String, Object> response = new HashMap<>();
    response.put("status", "success");
    response.put("data", cashiers);
    return response;
   }


   @PostMapping ("/cashier")
   public String create(){
    return "Hi";
   }
   @PutMapping ("/cashier")
   public String update(){
    return "Hi";
   }
   @DeleteMapping ("/cashier")
    public String delete(){
     return "Hi";
    }


}
/* overrid

 */
