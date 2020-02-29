import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.grandplan.planner.models.Login;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService{
    private Login loginUser;
    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Autowired
    public LoginService(Login user){
        this.loginUser = user;
    }

    public boolean validateLogin(Login user, Model model){
        if(user.getEmail() == "" || user.getPassword() == ""){
          model.addAttribute("emailError", "Please provide your email");
          model.addAttribute("passwordError", "Please provide your password");
          return false;
        }
    
        if(!validateEmail(user.getEmail())){
          model.addAttribute("emailError", "Please provide a valid email");
          return false;
        }
    
        return true;
    }
    
    public boolean validateEmail(String email){
        if(email == "") return false;
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean findUser(Login user){
        return true;
    }
}