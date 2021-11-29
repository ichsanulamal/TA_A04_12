package apap.tugas.siretail.controller;

import apap.tugas.siretail.model.ItemCabangModel;
import apap.tugas.siretail.rest.ListSiItemModel;
import apap.tugas.siretail.rest.SiItemModel;
import apap.tugas.siretail.service.ItemCabangService;
import apap.tugas.siretail.service.ItemRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ItemController {
    @Autowired
    private ItemRestService itemRestService;

    @Autowired
    private ItemCabangService itemCabangService;

    @GetMapping("/item/{idCabang}/add")
    public String addItemForm(Model model, @PathVariable Integer idCabang) {
        List<SiItemModel> listItem = itemRestService.getListItemFromSiItem();
        model.addAttribute("listItem", listItem);

        ListSiItemModel listAddItem = new ListSiItemModel();
        List<SiItemModel> listIt = new ArrayList<>();
        listIt.add(new SiItemModel());
        listAddItem.setListSiItem(listIt);

        model.addAttribute("listAddItem", listAddItem);
//        return "form-add-item";
        return "tesajax";

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

    @GetMapping("/item/delete/{id}")
    public String deleteItem(@PathVariable int id, Model model) {
        ItemCabangModel item = itemCabangService.getItemCabangById(id);
        int cabangId = item.getCabang().getId();
        itemCabangService.deleteItemCabang(item);
        return "redirect:/cabang/view/" + cabangId;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String>  all() {
        return itemRestService.getListSiItem();
    }
}
