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
import com.fasterxml.jackson.databind.util.JSONPObject;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public void addItem(int idCabang, SiItemModel item) {
        SiItemModel siItemModel = getSiItemModel(item.getUuid());
        System.out.println(siItemModel.getStok());
        System.out.println(item.getStok());
        if (item.getStok() > siItemModel.getStok()) {
            return;
        }

        ItemCabangModel itemCabang = itemCabangDb.getByItemID(item.getUuid());
        if ( itemCabang != null ) {
            itemCabang.setStok(itemCabang.getStok() + item.getStok());
            itemCabangDb.save(itemCabang);
        } else {
            itemCabang = new ItemCabangModel();

            CabangModel cabang = cabangDb.getById(idCabang);
            itemCabang.setCabang(cabang);
            itemCabang.setItemID(item.getUuid());
            itemCabang.setStok(item.getStok());

            itemCabang.setNama(siItemModel.getNama());
            itemCabang.setHarga(siItemModel.getHarga());
            itemCabang.setKategori(siItemModel.getKategori());

            itemCabangDb.save(itemCabang);
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
        int stokSiItem = siItemModel.getStok() - item.getStok();
        HashMap reqBody = new HashMap<>();
        reqBody.put("stok", stokSiItem);

        System.out.println(this.webClient
                .put()
                .uri("/" + siItemModel.getUuid())
                .body(Mono.just(reqBody), HashMap.class)
                .retrieve()
                .bodyToMono(HashMap.class)
                .block());
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

    @Override
    public ResponseEntity<String> getListSiItem() {
        return this.webClient
                .get()
                .uri("/")
                .retrieve()
                .toEntity(String.class)
                .block();
    }
}
