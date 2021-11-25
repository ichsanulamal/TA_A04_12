package apap.tugas.siretail.controller;

import apap.tugas.siretail.model.RoleModel;
import apap.tugas.siretail.model.UserModel;
import apap.tugas.siretail.service.RoleService;
import apap.tugas.siretail.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/add")
    private String addUserForm(Model model) {
        UserModel user = new UserModel();
        List<RoleModel> listRole = roleService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("listRole", listRole);
        return "form-add-user";
    }

    @PostMapping(value = "/add")
    private String addUserSubmit(@ModelAttribute UserModel userModel,
                                 RedirectAttributes attributes) {
        String message = userService.addUser(userModel);
        attributes.addFlashAttribute("notif", message);
        return "redirect:/";
    }

    @GetMapping(value = "/update-password")
    private String updatePasswordForm(
            Model model){
        return "update-password";
    }

    @PostMapping(value = "/update-password")
    private String updatePasswordSubmit(
            @RequestParam(value = "oldPassword") String oldPassword,
            @RequestParam(value = "newPassword") String newPassword,
            @RequestParam(value = "newPasswordConfirmation") String newPasswordConfirmation,
            HttpServletRequest request,
            RedirectAttributes attributes,
            Model model
    ) {
        Principal principal = request.getUserPrincipal();
        UserModel user = userService.findUserByUsername(principal.getName());
        if(!newPassword.equals(newPasswordConfirmation)){
            model.addAttribute("notif", "Password baru tidak match, mohon input ulang");
            return "update-password";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(passwordEncoder.matches(oldPassword, user.getPassword())){
            String message = userService.updateUserPassword(user, newPassword);
            attributes.addFlashAttribute("notif", message);
            return "redirect:/user/update-password";
        }
        else{
            model.addAttribute("notif", "Password lama salah, mohon input ulang");
            return "update-password";
        }
    }

    @GetMapping("/viewall")
    public String viewAllUser(
            Model model
    ){
        return "viewall-user";
    }
}
