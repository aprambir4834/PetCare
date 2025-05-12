package PetCare.PetCare.Controllers;

import PetCare.PetCare.Connections.DBLoader;
import PetCare.PetCare.Connections.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/getowners")
    public String getowners() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from shop");
        return ans;
    }

    @GetMapping("/approveshopowner")
    public String approveshopowner(@RequestParam String id) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from shop where id='" + id + "'");
            if (rs.next()) {
                rs.moveToCurrentRow();
                rs.updateString("status", "approved");
                rs.updateRow();
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @GetMapping("/blockshopowner")
    public String blockshopowner(@RequestParam String id) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from shop where id='" + id + "'");
            if (rs.next()) {
                rs.moveToCurrentRow();
                rs.updateString("status", "pending");
                rs.updateRow();
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @GetMapping("/report")
    public String report() {
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT s.shopname,s.shopemail,s.shopphoto,SUM(bd.amnt) AS total_amount FROM  bill_detail bd JOIN shopproducts sp ON bd.pid = sp.id JOIN  shop s ON sp.sid = s.id GROUP BY  s.id, s.shopname, s.shopemail, s.shopphoto");
        return ans;
    }

}
