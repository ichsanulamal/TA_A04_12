package apap.tugas.siretail.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class SiItemModel {
    String uuid;
    String nama;
    int harga;
    int stok;
    String kategori;
}
