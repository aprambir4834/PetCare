package PetCare.PetCare.Controllers;

import PetCare.PetCare.Connections.DBLoader;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class UserController {

    @Autowired
    public EmailSenderService email;

    @GetMapping("/")
    public String index() {
        try {
            String today = java.time.LocalDate.now().toString();
            File file = new File("flag.txt");

            // If file does not exist, create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // Read the date from file
            BufferedReader br = new BufferedReader(new FileReader(file));
            String lastRunDate = br.readLine();
            br.close();

            if (!today.equals(lastRunDate)) {
                // Run reminder function
                ResultSet rs = DBLoader.executeSQL("SELECT a.date, u.email ,a.type,a.time FROM appointment a JOIN pets p ON a.did = p.did\n"
                        + "JOIN user u ON p.uid = u.id\n"
                        + "WHERE a.date = CURDATE(); ");

                while (rs.next()) {
                    String email = rs.getString("email");
                    String type = rs.getString("type");
                    String time = rs.getString("time");

                    String subject = "Appointment Reminder: " + type + " - " + today;
                    String body = "Dear Pet Owner,\n\n"
                            + "This is a kind reminder that you have a '" + type + "' appointment scheduled today at " + time + ".\n\n"
                            + "Please make sure to arrive on time. If you have any questions or need to reschedule, feel free to contact us.\n\n"
                            + "Thank you,\n"
                            + "Pet Care Team";

                    this.email.sendSimpleEmail(email, body, subject);
                }

                // Write today's date into flag.txt
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                bw.write(today);
                bw.close();
            }

        } catch (Exception ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "home";
    }

    @GetMapping("/uregister")
    public String uregister() {
        return "UserSignup";
    }

    @GetMapping("/UL")
    public String ul() {
        return "UserLogin";
    }

    @GetMapping("/UP")
    public String up() {
        return "UserProducts";
    }

    @GetMapping("/UPD")
    public String upd() {
        return "UserProductDetail";
    }

    @GetMapping("/cart")
    public String cart() {
        return "UserCart";
    }

    @GetMapping("/petdashboard")
    public String petdashboard() {
        return "PetDashboard";
    }

    @GetMapping("/pethome")
    public String pethome() {
        return "PetHome";
    }

    @GetMapping("/pvg")
    public String pvg() {
        return "PetVaccination";
    }

    @GetMapping("/pap")
    public String pap() {
        return "PetAppointment";
    }

    @GetMapping("/nv")
    public String nearbyvets() {
        return "nearbyvets";
    }
    
    @GetMapping("/addphoto")
    public String addphoto()
    {
        return "MngPetPhotos";
    }
    
    @GetMapping("/gallery")
    public String gallery()
    {
        return "gallery";
    }
    
    @GetMapping("/ushophistory")
    public String ush()
    {
        return "UserShopHistory";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/";
    }
    
    @GetMapping("/post")
    public String post()
    {
        return "posts";
    }
    
    @GetMapping("/nh")
    public String nh()
    {
        return "nearbyhospital";
    }
    
    @GetMapping("/gpt")
    public String gpt()
    {
        return "gpt";
    }
    
    @GetMapping("/ucp")
    public String userchangepassword()
    {
        return "UserChangePassword";
    }

}
