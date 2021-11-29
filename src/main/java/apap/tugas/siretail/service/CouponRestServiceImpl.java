package apap.tugas.siretail.service;

import apap.tugas.siretail.rest.CouponRestModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;

@Service
@Transactional
public class CouponRestServiceImpl implements CouponRestService{
    private final WebClient webSiBisnis;

    @Override
    public List<CouponRestModel> getListCoupon() {

        List<CouponRestModel> listCoupon = (List<CouponRestModel>) webSiBisnis.get().uri("/api/coupon/").retrieve().bodyToMono(CouponRestModel.class).block();
        return listCoupon;
    }

    public CouponRestServiceImpl(WebClient.Builder webClientBuilder) {
        this.webSiBisnis = webClientBuilder.baseUrl("https://api.agify.io/").build();
    }

}
