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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        ItemCabangModel itemCabang = itemCabangDb.getByItemID(item.getUuid());
        if ( itemCabang != null ) {
            itemCabang.setStok(itemCabang.getStok() + itemCabang.getStok());
            itemCabangDb.save(itemCabang);
            return;
        }
        ItemCabangModel newItemCabang = new ItemCabangModel();

        CabangModel cabang = cabangDb.getById(idCabang);
        newItemCabang.setCabang(cabang);
        newItemCabang.setItemID(item.getUuid());
        newItemCabang.setStok(item.getStok());
        newItemCabang.setKategori(item.getKategori());
        newItemCabang.setNama(item.getNama());
        itemCabangDb.save(newItemCabang);

        System.out.println(this.webClient
                .post()
                .uri("/")
                .body(Mono.just(item), SiItemModel.class)
                .retrieve()
                .bodyToMono(Map.class).block());
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
