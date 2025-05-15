package PetCare.PetCare.Controllers;

import PetCare.PetCare.Connections.DBLoader;
import PetCare.PetCare.Connections.RDBMS_TO_JSON;
import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.sql.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

//
//@RequestMapping("/api")
//@CrossOrigin(origins = "*")
@RestController
public class UserRestController {

    @Autowired
    public EmailSenderService email;

    @PostMapping("/usignup")
    public String usignup(@RequestParam String name,
            @RequestParam String email,
            @RequestParam String pass,
            @RequestParam MultipartFile photo) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from user where email='" + email + "'");
            if (rs.next()) {
                return "fail";
            } else {
                String ophoto = photo.getOriginalFilename();
                byte b[] = photo.getBytes();
                String abspath = "src/main/resources/static/myuploads/";
                FileOutputStream fos = new FileOutputStream(abspath + ophoto);
                fos.write(b);
                rs.moveToInsertRow();
                rs.updateString("name", name);
                rs.updateString("email", email);
                rs.updateString("pass", pass);
                rs.updateString("photo", ophoto);
                rs.insertRow();
                String body = "Welcome to PetCare! 🐾\n"
                        + "\n"
                        + "We’re excited to have you as part of our growing pet-loving community. At PetCare, we believe every pet deserves love, care, and attention — and we’re here to help you every step of the way.\n"
                        + "\n"
                        + "As a registered user, you can now:\n"
                        + "- Book appointments with trusted vets and groomers\n"
                        + "- Access expert pet care tips and articles\n"
                        + "- Track your pet’s vaccination and health records\n"
                        + "\n"
                        + "Need help or have questions? We’re just a message away!\n"
                        + "\n"
                        + "Thank you for choosing PetCare.\n"
                        + "Warm wags and regards,  \n"
                        + "The JC Pawfect Team\n"
                        + "\n"
                        + "--------------------------------------------\n"
                        + "This is an automated message. Please do not reply to this email.";

                String subject = "Welcome to JC Pawfect Care – Your Pet’s New Best Friend!";

                this.email.sendSimpleEmail(email, body, subject);

                return "success";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }

    }

    @PostMapping("/userlogin2")
    public String userlogin(@RequestParam String email, @RequestParam String pass, HttpSession session) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from user where email='" + email + "' and pass='" + pass + "'");
            if (rs.next()) {
                int id = rs.getInt("id");
                session.setAttribute("uid", id);
                session.setAttribute("uemail", email);
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }

    @GetMapping("/getprods")
    public String getprods() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from shopproducts");
        return ans;
    }

    @GetMapping("/pdetail")
    public String pdetail(@RequestParam String id) {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from shopproducts where id='" + id + "'");
        return ans;
    }

    @GetMapping("/atc")
    public String atc(@RequestParam String id, HttpSession session) {
        int op = 0;
        try {
            Integer uid = (Integer) session.getAttribute("uid");
            ResultSet rs = DBLoader.executeSQL("select * from cart where uid='" + uid + "' and pid='" + id + "'");
            if (rs.next()) {
                return "fail";
            } else {
                ResultSet rs2 = DBLoader.executeSQL("select * from shopproducts where id='" + id + "'");
                if (rs2.next()) {
                    String offerprice = rs2.getString("pofferprice");
                    op = Integer.parseInt(offerprice);
                }
                rs.moveToInsertRow();
                rs.updateInt("uid", uid);
                rs.updateInt("pid", Integer.parseInt(id));
                rs.updateInt("qty", 1);
                rs.updateInt("amnt", op);
                rs.insertRow();
                return "success";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }

    @GetMapping("/getcart")
    public String getcart(HttpSession session) {
        Integer uid = (Integer) session.getAttribute("uid");
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT s.pname,s.pphoto ,c.* from cart c inner join shopproducts s on s.id=c.pid where c.uid='" + uid + "'");
        return ans;
    }

    @GetMapping("/checkqty")
    public String checkqty(@RequestParam String id, @RequestParam String qty) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from cart where id='" + id + "'");
            if (rs.next()) {
                int pid = rs.getInt("pid");

                ResultSet rs2 = DBLoader.executeSQL("select * from shopproducts where id='" + pid + "'");  // fixed

                if (rs2.next()) {
                    int pqty = Integer.parseInt(rs2.getString("pqty"));
                    int cqty = Integer.parseInt(qty);
                    int price = Integer.parseInt(rs2.getString("pofferprice")); // fixed

                    if (cqty <= pqty) {
                        rs.moveToCurrentRow();
                        rs.updateInt("qty", cqty);
                        int newamnt = price * cqty;
                        rs.updateInt("amnt", newamnt);
                        rs.updateRow();
                        return "success";
                    } else {
                        return "fail"; // stock not enough
                    }
                } else {
                    return "fail"; // product not found
                }
            } else {
                return "fail"; // cart id not found
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString(); // exception case
        }
    }

    @PostMapping("/pay")
    public String pay(@RequestParam String name,
            @RequestParam String email,
            @RequestParam String contact,
            @RequestParam String address,
            @RequestParam String total,
            @RequestParam String type,
            HttpSession session) {
        Integer uid = (Integer) session.getAttribute("uid");
        try {
            ResultSet rs = DBLoader.executeSQL("select * from bill");
            rs.moveToInsertRow();
            rs.updateString("name", name);
            rs.updateString("email", email);
            rs.updateString("contact", contact);
            rs.updateString("type", type);
            rs.updateString("address", address);
            rs.updateString("total", total);
            rs.updateInt("uid", uid);
            rs.insertRow();
            ResultSet rs2 = DBLoader.executeSQL("SELECT MAX(id) AS maxid FROM bill");
            if (rs2.next()) {
                int mid = rs2.getInt("maxid");
                ResultSet rs3 = DBLoader.executeSQL("select * from cart where uid='" + uid + "'");
                ResultSet rs4 = DBLoader.executeSQL("select * from bill_detail");
                while (rs3.next()) {
                    int pid = rs3.getInt("pid");
                    int qty = rs3.getInt("qty");
                    int amnt = rs3.getInt("amnt");
                    rs4.moveToInsertRow();
                    rs4.updateInt("b_id", mid);
                    rs4.updateInt("pid", pid);
                    rs4.updateInt("qty", qty);
                    rs4.updateInt("amnt", amnt);
                    rs4.insertRow();
                    ResultSet rs5 = DBLoader.executeSQL("select * from shopproducts where id='" + pid + "'");
                    if (rs5.next()) {
                        int uqty = Integer.parseInt(rs5.getString("pqty"));
                        uqty -= qty;
                        rs5.moveToCurrentRow();
                        rs5.updateString("pqty", uqty + "");
                        rs5.updateRow();
                    }
                    rs3.deleteRow();
                }
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }

    @PostMapping("/addpet")
    public String addpet(@RequestParam String name,
            @RequestParam String dob,
            @RequestParam String sex,
            @RequestParam String type,
            @RequestParam String breed,
            @RequestParam MultipartFile photo, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("uid");
        try {
            ResultSet rs = DBLoader.executeSQL("select * from pets where name ='" + name + "'");
            if (rs.next()) {
                return "fail";
            } else {
                String ophoto = photo.getOriginalFilename();
                byte b[] = photo.getBytes();
                String abspath = "src/main/resources/static/myuploads/";
                FileOutputStream fos = new FileOutputStream(abspath + ophoto);
                fos.write(b);
                rs.moveToInsertRow();
                rs.updateString("name", name);
                rs.updateString("dob", dob);
                rs.updateString("breed", breed);
                rs.updateString("gender", sex);
                rs.updateString("type", type);
                rs.updateString("photo", ophoto);
                rs.updateInt("uid", uid);
                rs.insertRow();
                return "success";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }

    @GetMapping("/getpets")
    public String getpets(HttpSession session) {
        Integer uid = (Integer) session.getAttribute("uid");
        String ans = new RDBMS_TO_JSON().generateJSON("select * from pets where uid='" + uid + "'");
        return ans;
    }

    @PostMapping("/addvaccine")
    public String addvaccine(@RequestParam String name,
            @RequestParam String date,
            @RequestParam String purpose,
            @RequestParam String did) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from vaccination where name='" + name + "' and date='" + date + "' and did='" + did + "'");
            if (rs.next()) {
                return "fail";
            } else {
                rs.moveToInsertRow();
                rs.updateString("name", name);
                rs.updateString("date", date);
                rs.updateString("purpose", purpose);
                rs.updateInt("did", Integer.parseInt(did));
                rs.insertRow();
                return "success";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }

    @GetMapping("/getvaccine")
    public String getvaccine(@RequestParam String id) {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from vaccination where did='" + id + "'");
        return ans;
    }

    @GetMapping("/deletevaccine")
    public String deletevaccine(@RequestParam String id) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from vaccination where id='" + id + "'");
            if (rs.next()) {
                rs.deleteRow();
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }
    
     @GetMapping("/deleteappointment")
    public String deleteappointment(@RequestParam String id) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from appointment where id='" + id + "'");
            if (rs.next()) {
                rs.deleteRow();
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }
    

    @PostMapping("/addappointment")
    public String addappointment(@RequestParam String date,
            @RequestParam String type,
            @RequestParam String time,
            @RequestParam String did,
            @RequestParam String desc) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from appointment");
            rs.moveToInsertRow();
            rs.updateString("date", date);
            rs.updateString("time", time);
            rs.updateString("desc", desc);
            rs.updateString("type", type);
            rs.updateInt("did", Integer.parseInt(did));
            rs.insertRow();
            return "success";
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }

    }

    @PostMapping("/petphoto")
    public String petphoto(@RequestParam MultipartFile photo, @RequestParam String did) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from photos");
            String ophoto = photo.getOriginalFilename();
            byte b[] = photo.getBytes();
            String abspath = "src/main/resources/static/myuploads/";
            FileOutputStream fos = new FileOutputStream(abspath + ophoto);
            fos.write(b);
            rs.moveToInsertRow();
            rs.updateString("photo", ophoto);
            rs.updateInt("did", Integer.parseInt(did));
            rs.insertRow();
            return "success";

        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }

    @GetMapping("/getphotos")
    public String getphoto(@RequestParam String id) {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from photos where did='" + id + "'");
        return ans;
    }

    @GetMapping("/order")
    public String order(HttpSession session) {
        Integer uid = (Integer) session.getAttribute("uid");
        String ans = new RDBMS_TO_JSON().generateJSON("select * from bill where uid='" + uid + "'");
        return ans;
    }

    @GetMapping("/billdetail")
    public String billdetail(@RequestParam String id) {
        String ans = new RDBMS_TO_JSON().generateJSON("select s.pphoto,s.pname , b.* from shopproducts s inner join bill_detail b on s.id=b.pid where b_id='" + id + "'");
        return ans;
    }

    @GetMapping("/getappoinment")
    public String getappoinment(@RequestParam String id) {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from appointment where did='" + id + "'");
        return ans;
    }

   

}
