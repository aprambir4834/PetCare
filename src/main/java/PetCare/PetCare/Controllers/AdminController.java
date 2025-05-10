
package PetCare.PetCare.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    
    @GetMapping("/adminlogin")
    public String adminlogin()
    {
        return "AdminLogin";
    }
    
    @GetMapping("/ahome")
    public String ahome()
    {
        return "AdminHome";
    }
    
    
}
