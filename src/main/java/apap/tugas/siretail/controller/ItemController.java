package apap.tugas.siretail.controller;

import apap.tugas.siretail.model.CabangModel;
import apap.tugas.siretail.rest.ListSiItemModel;
import apap.tugas.siretail.rest.SiItemModel;
import apap.tugas.siretail.service.ItemRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {
    @Autowired
    private ItemRestService itemRestService;

    @GetMapping("/item/{idCabang}/add")
    public String addItemForm(Model model, @PathVariable Integer idCabang) {
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
            Model model
    ) {
        for (SiItemModel siItem: listAddItem.getListSiItem()) {
            itemRestService.addItem(idCabang, siItem);
        }
        return "add-item";
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

        Integer rowId = Integer.valueOf(row);
        listAddItem.getListSiItem().remove(rowId.intValue());

        model.addAttribute("listItem", listItem);
        return "form-add-item";
    }
}
