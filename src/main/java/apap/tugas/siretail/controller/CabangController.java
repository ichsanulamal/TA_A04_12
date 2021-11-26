package apap.tugas.siretail.controller;

import apap.tugas.siretail.model.CabangModel;
import apap.tugas.siretail.model.ItemCabangModel;
import apap.tugas.siretail.model.UserModel;
import apap.tugas.siretail.service.CabangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;

@SuppressWarnings("ALL")
@Controller
public class CabangController {

    @Qualifier("cabangServiceImpl")
    @Autowired
    private CabangService cabangService;

//    @PostMapping(value = "/cabang/add", params = {"addRow"})
//    public String addRowCabangMultiple(
//            @ModelAttribute CabangModel cabang,
//            Model model
//    ) {
//        if(cabang.getListMenu() == null || cabang.getListMenu().size() == 0){
//            cabang.setListMenu(new ArrayList<>());
//        }
//        cabang.getListMenu().add(new MenuModel());
//        List<MenuModel> listMenu = menuService.getListMenu();
//
//        model.addAttribute("cabang", cabang);
//        model.addAttribute("listMenuExisting", listMenu);
//        return "form-add-cabang";
//    }
//
//    @PostMapping(value = "/cabang/add", params = {"deleteRow"})
//    public String deleteRowCabangMultiple(
//            @ModelAttribute CabangModel cabang,
//            @RequestParam("deleteRow") Integer row,
//            Model model
//    ) {
//        final Integer rowId = Integer.valueOf(row);
//        cabang.getListMenu().remove(rowId.intValue());
//
//        List<MenuModel> listMenu = menuService.getListMenu();
//
//        model.addAttribute("cabang", cabang);
//        model.addAttribute("listMenuExisting", listMenu);
//        return "form-add-cabang";
//    }

    @GetMapping("/cabang/view/{id}")
    public String viewDetailCabang(
            @PathVariable(value = "id") int id,
            Model model
    ) {
        CabangModel cabang = cabangService.getCabangByIdCabang(id);
        model.addAttribute("cabang", cabang);

        return "view-cabang";
    }

    @GetMapping("/cabang/viewall")
    public String viewAllCabang(Authentication authentication, Model model) {
        List<CabangModel> listCabang = new ArrayList<CabangModel>();
        if (authentication.getAuthorities().equals("ROLE_Manager Cabang")){
            listCabang = cabangService.getAllCabangByManager();
        } else {
            listCabang = cabangService.getAllCabang();
        }
        model.addAttribute("listCabang", listCabang);
        return "view-all-cabang";
    }

    @PostMapping("/cabang/delete/{id}")
    public String deleteCabang(
        @PathVariable(value = "id") int id,
        Model model
    ) {
        CabangModel cabang = cabangService.getCabangByIdCabang(id);
        if (cabang.getStatus() == 0) {
            return "cannot-delete-cabang";
        } else if (cabang.getListItem().size() > 0) {
            return "cannot-delete-cabang";
        } else {
            cabangService.deleteCabang(cabang);
            return "success-deleted-cabang";
        }
    }

    @GetMapping("/cabang/update/{id}")
    public String updateCabangForm(
            @PathVariable int id,
            Model model
    ) {
        CabangModel cabang = cabangService.getCabangByIdCabang(id);
        model.addAttribute("cabang", cabang);
        return "form-update-cabang";
    }

    @PostMapping("/cabang/update")
    public String updateCabangSubmit(
            @ModelAttribute CabangModel cabang,
            Model model
    ) {
        cabangService.updateCabang(cabang);
        model.addAttribute("noCabang", cabang.getId());
        return "update-cabang";
    }
}

