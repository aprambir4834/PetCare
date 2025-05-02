
package PetCare.PetCare.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopController {
    @GetMapping("/SRegister")
    public String srome()
    {
        return "ShopRegister";
    }
    
    @GetMapping("/SHome")
    public String shome()
    {
        return "ShopHome";
    }
    
    @GetMapping("/slogin")
    public String slogin()
    {
        return "ShopLogin";
    }
    
    @GetMapping("/sproducts")
    public String sproducts()
    {
        return "ShopAddProducts";
    }
    
    @GetMapping("/allproducts")
    public String allproducts()
    {
        return "ShopAllProducts";
    }
    @GetMapping("/seditproducts")
    public String seditproducts()
    {
        return "ShopEditProducts";
    }
    
    @GetMapping("/shopreport")
    public String shopreport()
    {
        return "ShopReport";
    }
    
}
