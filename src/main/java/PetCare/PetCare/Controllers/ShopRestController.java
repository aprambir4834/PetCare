package PetCare.PetCare.Controllers;

import PetCare.PetCare.Connections.DBLoader;
import PetCare.PetCare.Connections.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ShopRestController {

    @Autowired
    public EmailSenderService email;

    @PostMapping("/sregister")
    public String sregister(
            @RequestParam String sname,
            @RequestParam String semail,
            @RequestParam String scontact,
            @RequestParam String sgst,
            @RequestParam MultipartFile sphoto,
            @RequestParam String slocation,
            @RequestParam String oname,
            @RequestParam String oemail,
            @RequestParam String ocontact,
            @RequestParam String odob,
            @RequestParam MultipartFile ophoto, @RequestParam String pass
    ) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from shop where shopemail='" + semail + "'");
            if (rs.next()) {
                return "fail";
            } else {
                String shophoto = sphoto.getOriginalFilename();
                byte b[] = sphoto.getBytes();
                String abspath = "src/main/resources/static/myuploads/";
                FileOutputStream fos = new FileOutputStream(abspath + oname);
                fos.write(b);
                String oname1 = ophoto.getOriginalFilename();
                byte b1[] = ophoto.getBytes();
                String abspath1 = "src/main/resources/static/myuploads/";
                FileOutputStream fos1 = new FileOutputStream(abspath1 + oname1);
                fos1.write(b1);
                rs.moveToInsertRow();
                rs.moveToInsertRow();

                rs.updateString("shopname", sname);
                rs.updateString("shoplocation", slocation);
                rs.updateString("shopphoto", shophoto);
                rs.updateString("gstnumber", sgst);
                rs.updateString("shopemail", semail);
                rs.updateString("shopcontact", scontact);

                rs.updateString("ownername", oname);
                rs.updateString("ownerphoto", oname1);
                rs.updateString("ownercontact", ocontact);
                rs.updateString("owneremail", oemail);
                rs.updateString("ownerdob", odob);
                rs.updateString("status", "pending");
                rs.updateString("pass", pass);
                rs.insertRow();
                String body = """
Dear Shopkeeper,

Thank you for signing up as a seller on PetCare! 🐾

We’ve received your request to sell pet-related products on our platform.
Your registration details have been successfully forwarded to our admin team for review.

Once reviewed, the admin will either approve or block your request based on the submitted information.
You will be notified via email once the decision is made.

Thank you for choosing PetCare.
We look forward to having you onboard!

Warm regards,
The PetCare Team

--------------------------------------------
This is an automated message. Please do not reply to this email.
""";

                String subject = "Thank You for Registering on PetCare – Awaiting Approval";
                email.sendSimpleEmail(semail, body, subject);

                return "success";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }

    }

    @PostMapping("/shoplogin")
    public String shoplogin(@RequestParam String email, @RequestParam String pass, HttpSession session) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from shop where shopemail='" + email + "' and pass='" + pass + "'");
            if (rs.next()) {
                int id = rs.getInt("id");
                session.setAttribute("sid", id);
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @PostMapping("/addproduct")
    public String addProduct(
            @RequestParam String pname,
            @RequestParam String price,
            @RequestParam String offerprice,
            @RequestParam String qty,
            @RequestParam String shortdesc,
            @RequestParam String longdesc,
            @RequestParam MultipartFile photo, HttpSession session
    ) {
        Integer sid = (Integer) session.getAttribute("sid");

        try {
            ResultSet rs = DBLoader.executeSQL("select * from shopproducts where pname='" + pname + "' and sid='" + sid + "'");
            if (rs.next()) {
                return "fail";
            } else {
                String oname = photo.getOriginalFilename();
                byte b[] = photo.getBytes();
                String abspath = "src/main/resources/static/myuploads/";
                FileOutputStream fos = new FileOutputStream(abspath + oname);
                fos.write(b);
                rs.moveToInsertRow();
                rs.updateInt("sid", sid);
                rs.updateString("pname", pname);
                rs.updateString("pprice", price);
                rs.updateString("pofferprice", offerprice);
                rs.updateString("pqty", qty);
                rs.updateString("pshortdesc", shortdesc);
                rs.updateString("plongdesc", longdesc);
                rs.updateString("pphoto", oname);

                rs.insertRow();
                return "success";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }

    }

    @GetMapping("/getproducts")
    public String getproducts(HttpSession session) {
        Integer sid = (Integer) session.getAttribute("sid");
        String ans = new RDBMS_TO_JSON().generateJSON("select * from shopproducts where sid='" + sid + "'");
        return ans;
    }

    @GetMapping("/geteditproducts")
    public String geteditproducts(@RequestParam String id) {

        String ans = new RDBMS_TO_JSON().generateJSON("select * from shopproducts where id='" + id + "'");
        return ans;
    }

    @GetMapping("/deleteproduct")
    public String deleteproduct(@RequestParam String id) {
        try {
            ResultSet rs = DBLoader.executeSQL("select * from shopproducts where id='" + id + "'");
            if (rs.next()) {
                rs.deleteRow();
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @PostMapping("/editproductshop")
    public String editp(
            @RequestParam String pname,
            @RequestParam String price,
            @RequestParam String offerprice,
            @RequestParam String qty,
            @RequestParam String shortdesc,
            @RequestParam String longdesc,
            @RequestParam String id
    ) {

        try {
            ResultSet rs = DBLoader.executeSQL("select * from shopproducts where id='" + id + "'");
            if (rs.next()) {
                rs.moveToCurrentRow();

                rs.updateString("pname", pname);
                rs.updateString("pprice", price);
                rs.updateString("pofferprice", offerprice);
                rs.updateString("pqty", qty);
                rs.updateString("pshortdesc", shortdesc);
                rs.updateString("plongdesc", longdesc);

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

    @GetMapping("/getsales")
    public String getsales(HttpSession session) {
        Integer sid = (Integer) session.getAttribute("sid");
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT \n"
                + "  b.id AS bill_id,b.address, b.name,\n"
                + "  bd.pid AS product_id,\n"
                + "  p.pname AS product_name,\n"
                + "  p.pphoto AS product_photo,\n"
                + "  p.sid AS store_id,\n"
                + "  bd.qty,\n"
                + "  bd.amnt\n"
                + "FROM bill b\n"
                + "JOIN bill_detail bd ON b.id = bd.b_id\n"
                + "JOIN shopproducts p ON bd.pid = p.id\n"
                + "WHERE p.sid = '" + sid + "'");
        return ans;
    }

}
