package apap.tugas.siretail.controller;

import apap.tugas.siretail.model.CabangModel;
import apap.tugas.siretail.model.ItemCabangModel;
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
}

