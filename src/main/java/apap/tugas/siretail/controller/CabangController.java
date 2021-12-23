package apap.tugas.siretail.controller;

import apap.tugas.siretail.model.CabangModel;
import apap.tugas.siretail.model.UserModel;
import apap.tugas.siretail.repository.CabangDb;
import apap.tugas.siretail.service.CabangService;
import apap.tugas.siretail.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SuppressWarnings("ALL")
@Controller
public class CabangController {

    @Qualifier("cabangServiceImpl")
    @Autowired
    private CabangService cabangService;

    @Autowired
    private UserService userService;

    @Autowired
    private CabangDb cabangDb;

    private WebClient webClient;
    
    @GetMapping("/cabang/add") 
    public String addCabangForm(Model model) {
        model.addAttribute("cabang", new CabangModel());
        return "form-add-cabang";
    }

    @PostMapping("/cabang/add")
    public String addCabangSubmit(@ModelAttribute CabangModel cabang, HttpServletRequest request, Model model) {
        Principal principal = request.getUserPrincipal();
        UserModel currentUser = userService.findUserByUsername(principal.getName());
        
        cabang.setPenanggungJawab(currentUser);
        cabang.setStatus(2); // status cabang 2 = DISETUJUI

        cabangService.addCabang(cabang);
        model.addAttribute("cabang", cabang);
        model.addAttribute("msg", 1);
        return "form-add-cabang";
    }

    

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
        if (authentication.getAuthorities().toString().equals("[Manager Cabang]")){
            // try {
            List<CabangModel> dataCabang = cabangService.getAllCabang();
            listCabang = cabangService.getAllCabangByManager(authentication.getName().toString(), dataCabang);
            // } catch (NullPointerException e) {
                
            // }
            
        } else {
            listCabang = cabangService.getAllCabang();
        }
        model.addAttribute("listCabang", listCabang);

        //izin nambahin buat code gw
        PageController.msg = 0;

        return "view-all-cabang";
    }

    @GetMapping("/cabang/delete/{id}")
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
        model.addAttribute("cabang", cabang);
        model.addAttribute("msg", 1);
        return "form-update-cabang";
    }

    @GetMapping("/request-item/{id}")
    public String requestStokItemForm(
        @PathVariable int id,
        Model model
    ) {
        CabangModel cabang = cabangService.getCabangByIdCabang(id);
        model.addAttribute("cabang", cabang);
        model.addAttribute("id", id);
        return "form-update-stok-item";
    }

    @GetMapping("/cabang/permintaan-cabang")
    public String permintaanCabang(
            Model model
    ) {
        List<CabangModel> listCabang = cabangService.getAllCabang();
        List<CabangModel> permintaanCabang = new ArrayList<>();

        for (CabangModel cabang: listCabang) {
            if (cabang.getStatus() == 0) {
                permintaanCabang.add(cabang);
            }
        }
        model.addAttribute("msg", PageController.msg);
        model.addAttribute("permintaanCabang", permintaanCabang);
        return "view-permintaan-cabang";
    }

    @GetMapping("/cabang/setuju-permintaan/{id}")
    public String setujuPermintaan(
            @PathVariable int id,
            HttpServletRequest request,
            Model model
    ) {
       CabangModel cabang = cabangService.getCabangByIdCabang(id);
       Principal principal = request.getUserPrincipal();
       UserModel currentUser = userService.findUserByUsername(principal.getName());

       cabang.setStatus(2);
       cabang.setPenanggungJawab(currentUser);
       cabangDb.save(cabang);

        PageController.msg = 1;

        return "redirect:/cabang/permintaan-cabang";
    }

    @GetMapping("/cabang/tolak-permintaan/{id}")
    public String tolakPermintaan(
            @PathVariable int id,
            HttpServletRequest request,
            Model model
    ) {
        CabangModel cabang = cabangService.getCabangByIdCabang(id);
        cabangService.deleteCabang(cabang);


        PageController.msg = 2;

        return "redirect:/cabang/permintaan-cabang";
    }

    @PostMapping("/cabang/request-item")
    public String requestItemStokSubmit(
            Model model,
            @RequestParam("idItem") String idItem,
            @RequestParam("jumlah_stok") int jumlah_stok,
            @RequestParam("idCabang") int idCabang
    ) {
        HashMap reqBody = new HashMap<>();
        reqBody.put("id_item", idItem);
        reqBody.put("tambahan_stok", jumlah_stok);
        reqBody.put("id_cabang", idCabang);
        
        WebClient webClient = WebClient.create("https://sifactory-a04.herokuapp.com/api");

        System.out.println(
        webClient
            .post()
            .uri("/request/updateItem")
            .body(Mono.just(reqBody), HashMap.class)
            .retrieve()
            .bodyToMono(HashMap.class)
            .block()
        );
        model.addAttribute("msg", 1);
        return "success-request";
    }

    
}

