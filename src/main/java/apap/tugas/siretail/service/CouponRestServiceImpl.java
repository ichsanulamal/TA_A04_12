package apap.tugas.siretail.service;

import apap.tugas.siretail.rest.CouponRestModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Transactional
public class CouponRestServiceImpl implements CouponRestService{
    private final WebClient webSiBisnis;

    @Override
    public List<CouponRestModel> getListCoupon() {
        LinkedHashMap<String, List<CouponRestModel>> resultJson = (LinkedHashMap<String, List<CouponRestModel>>) webSiBisnis.get().uri("/api/list-couponToday").retrieve().bodyToMono(Object.class).block();
        List<CouponRestModel> listCoupon = resultJson.get("result");
        return listCoupon;
    }

    public CouponRestServiceImpl(WebClient.Builder webClientBuilder) {
        this.webSiBisnis = webClientBuilder.baseUrl("https://si-business.herokuapp.com").build();
    }

}
