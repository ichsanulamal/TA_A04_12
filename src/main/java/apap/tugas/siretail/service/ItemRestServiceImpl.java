package apap.tugas.siretail.service;

import apap.tugas.siretail.model.CabangModel;
import apap.tugas.siretail.model.ItemCabangModel;
import apap.tugas.siretail.repository.CabangDb;
import apap.tugas.siretail.repository.ItemCabangDb;
import apap.tugas.siretail.rest.Setting;
import apap.tugas.siretail.rest.SiItemModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ItemRestServiceImpl implements ItemRestService {
    private final WebClient webClient;

    @Autowired
    private ItemCabangDb itemCabangDb;

    @Autowired
    private CabangDb cabangDb;

    public ItemRestServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl(Setting.siItemUrl).build();
    }

    @Override
    public Object addItem(int idCabang, SiItemModel itemAdd) {
        SiItemModel siItemModel = getSiItemModel(itemAdd.getUuid());
        System.out.println(siItemModel.getStok());
        System.out.println(itemAdd.getStok());
        if (itemAdd.getStok() > siItemModel.getStok()) {
            return siItemModel;
        }

        CabangModel cabang = cabangDb.getById(idCabang);
        ItemCabangModel itemCabang = itemCabangDb.getItemCabangModelByItemIDAndAndCabang(itemAdd.getUuid(), cabang);
        if (itemCabang != null) {
            itemCabang.setStok(itemCabang.getStok() + itemAdd.getStok());
            itemCabangDb.save(itemCabang);
        } else {
            itemCabang = new ItemCabangModel();

            itemCabang.setCabang(cabang);
            itemCabang.setItemID(itemAdd.getUuid());
            itemCabang.setStok(itemAdd.getStok());

            itemCabang.setNama(siItemModel.getNama());
            itemCabang.setHarga(siItemModel.getHarga());
            itemCabang.setKategori(siItemModel.getKategori());

            itemCabangDb.save(itemCabang);

            System.out.println("masuk sini bro");
        }
        // post
//        siItemModel.setStok(siItemModel.getStok() - item.getStok());
//        System.out.println(this.webClient
//                .post()
//                .uri("/")
//                .body(Mono.just(item), SiItemModel.class)
//                .retrieve()
//                .bodyToMono(Map.class).block());
        // put
        int stokSiItem = siItemModel.getStok() - itemAdd.getStok();
        HashMap reqBody = new HashMap<>();
        reqBody.put("stok", stokSiItem);

        System.out.println(this.webClient
                .put()
                .uri("/" + siItemModel.getUuid())
                .body(Mono.just(reqBody), HashMap.class)
                .retrieve()
                .bodyToMono(HashMap.class)
                .block());
        return itemCabang;
    }

    @Override
    public SiItemModel getSiItemModel(String uuid) {
        try {
            LinkedHashMap res = this.webClient
                    .get()
                    .uri("/" + uuid)
                    .retrieve()
                    .bodyToMono(LinkedHashMap.class).block();
            LinkedHashMap itemMap = (LinkedHashMap) (res.get("result"));
            ObjectMapper mapper = new ObjectMapper();
            SiItemModel item = mapper.convertValue(itemMap, SiItemModel.class);
            System.out.println(item);
            return item;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    @Override
    public List<SiItemModel> getListItemFromSiItem() {
        try {
            List<LinkedHashMap> listSiItem = (List<LinkedHashMap>)(this.webClient
                            .get()
                            .uri("/")
                            .retrieve()
                            .bodyToMono(Map.class).block().get("result"));

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.convertValue(listSiItem, JsonNode.class);
            List<SiItemModel> listSiItemFix = mapper.convertValue(jsonNode, new TypeReference<List<SiItemModel>>() {});
            return listSiItemFix;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

}
