package apap.tugas.siretail.service;

import apap.tugas.siretail.rest.CouponRestModel;

import java.util.List;

public interface CouponRestService {
    List<CouponRestModel> getListCoupon();
}