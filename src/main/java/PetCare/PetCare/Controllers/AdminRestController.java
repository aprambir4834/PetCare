package PetCare.PetCare.Controllers;

import PetCare.PetCare.Connections.DBLoader;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class AdminRestController {

    @PostMapping("/alogin")
    public String alogin(@RequestParam String uname, @RequestParam String pass, HttpSession session) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from admin where uname='" + uname + "' and pass='" + pass + "'");
            if (rs.next()) {
                session.setAttribute("aemail", uname);
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }
    
    
}
