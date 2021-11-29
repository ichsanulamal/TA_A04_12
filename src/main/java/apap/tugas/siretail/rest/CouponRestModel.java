package apap.tugas.siretail.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponRestModel {
    int id;
    String couponCode;
    String couponName;
    Double discountAmount;
    Date expiryDate;
}
