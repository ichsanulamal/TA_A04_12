package apap.tugas.siretail.controller;

import apap.tugas.siretail.model.CabangModel;
import apap.tugas.siretail.model.ItemCabangModel;
import apap.tugas.siretail.repository.ItemCabangDb;
import apap.tugas.siretail.rest.CouponRestModel;
import apap.tugas.siretail.rest.ListSiItemModel;
import apap.tugas.siretail.rest.SiItemModel;
import apap.tugas.siretail.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ItemController {
    @Autowired
    private ItemRestService itemRestService;

    @Autowired
    private ItemCabangService itemCabangService;

    @Autowired
    private CabangService cabangService;

    @Autowired
    private UserService userService;


    @Autowired
    private CouponRestService couponRestService;

    @Autowired
    private ItemCabangDb itemCabangDb;

//    @PreAuthorize("hasAuthority('Kepala Retail') or hasAuthority('Manager Cabang')")
    @GetMapping("/item/{idCabang}/add")
    public String addItemForm(Model model, @PathVariable Integer idCabang) {
        if (!isAuthorizedAddItem(idCabang)) {
            return "error/401";
        }

        List<SiItemModel> listItem = itemRestService.getListItemFromSiItem();
        model.addAttribute("listItem", listItem);

        ListSiItemModel listAddItem = new ListSiItemModel();
        List<SiItemModel> listIt = new ArrayList<>();
        listIt.add(new SiItemModel());
        listAddItem.setListSiItem(listIt);

        model.addAttribute("listAddItem", listAddItem);
        return "form-add-item";
    }

    @PostMapping(value = "/item/{idCabang}/add", params = {"save"})
    public String addItemSubmit(
            @ModelAttribute ListSiItemModel listAddItem,
            @PathVariable Integer idCabang,
            RedirectAttributes redirectAttributes
    ) {
        if (!isAuthorizedAddItem(idCabang)) {
            return "error/401";
        }

        List<String> notif = new ArrayList<>();
        for (SiItemModel siItem: listAddItem.getListSiItem()) {
            Object addItemObject = itemRestService.addItem(idCabang, siItem);
            if (addItemObject instanceof ItemCabangModel) {
                notif.add("Berhasil menambahkan item " + ((ItemCabangModel) addItemObject).getNama() + " sebanyak " + siItem.getStok());
            } else {
                notif.add("Gagal menambahkan item " + ((SiItemModel) addItemObject).getNama() + ", mohon periksa kembali stok Si-Item");
            }
        }
        redirectAttributes.addFlashAttribute("notifAddItem", notif);

        return "redirect:/cabang/view/" + idCabang;
    }

    @PostMapping(value = "/item/{idCabang}/add", params = {"addRow"})
    public String addRowItemMultiple(
            @ModelAttribute ListSiItemModel listAddItem,
            @PathVariable Integer idCabang,
            Model model
    ) {

        List<SiItemModel> listItem = itemRestService.getListItemFromSiItem();
        model.addAttribute("listItem", listItem);

        if(listAddItem.getListSiItem() == null || listAddItem.getListSiItem().size() == 0){
            listAddItem.setListSiItem(new ArrayList<>());
        }
        listAddItem.getListSiItem().add(new SiItemModel());
        model.addAttribute("listAddItem", listAddItem);
        return "form-add-item";
    }

    @PostMapping(value = "/item/{idCabang}/add", params = {"deleteRow"})
    public String deleteRowItemMultiple(
            @ModelAttribute ListSiItemModel listAddItem,
            @PathVariable Integer idCabang,
            @RequestParam("deleteRow") Integer row,
            Model model
    ) {
        List<SiItemModel> listItem = itemRestService.getListItemFromSiItem();
        model.addAttribute("listItem", listItem);

        if (listAddItem.getListSiItem().size() > 1) {
            Integer rowId = Integer.valueOf(row);
            listAddItem.getListSiItem().remove(rowId.intValue());
        } else {
            model.addAttribute("notif", "Anda perlu menambahkan minimal 1 item");
        }
        model.addAttribute("listAddItem", listAddItem);
        return "form-add-item";
    }

    @GetMapping("/item/delete/{id}")
    public String deleteItem(@PathVariable int id, Model model) {
        ItemCabangModel item = itemCabangService.getItemCabangById(id);
        int cabangId = item.getCabang().getId();
        itemCabangService.deleteItemCabang(item);
        return "redirect:/cabang/view/" + cabangId;
    }

    @GetMapping("/item/coupon/{id}")
    public String getListCoupon(@PathVariable int id, Model model) {
        List<CouponRestModel> listCoupon = couponRestService.getListCoupon();
        ItemCabangModel item = itemCabangService.getItemCabangById(id);
        model.addAttribute("listCoupon", listCoupon);
        model.addAttribute("item", item);
        return "viewall-coupon";
    }

    @GetMapping("/item/coupon/{idItem}/{idCoupon}/{discountAmount}")
    public String calculateCoupon(@PathVariable int idItem, @PathVariable int idCoupon, @PathVariable double discountAmount, Model model) {
        ItemCabangModel item = itemCabangService.getItemCabangById(idItem);
        int cabangId = item.getCabang().getId();

        item.setIdPromo(idCoupon);
        item.setHarga((int) (item.getHarga() - item.getHarga()*(discountAmount/100.0)));
        itemCabangDb.save(item);
        return "redirect:/cabang/view/" + cabangId;
    }

    private boolean isAuthorizedAddItem(int idCabang) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String role = ((UserDetails) principal).getAuthorities().toArray()[0].toString();
            if (role.equals("Kepala Retail")) return true;
            else if (role.equals("Manager Cabang"))  {
                CabangModel cabang = cabangService.getCabangByIdCabang(idCabang);
                try {
                    String username = ((UserDetails)principal).getUsername(); // error
                    if (cabang.getPenanggungJawab().getUsername().equals(username)) {
                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }
}
