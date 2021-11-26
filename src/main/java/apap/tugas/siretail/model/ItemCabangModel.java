package apap.tugas.siretail.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "item_cabang")
@JsonIgnoreProperties(value = {"cabang"}, allowSetters = true)
public class ItemCabangModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "uuid_item")
    private String itemID;

//    @NotNull
    @Size(max = 50)
    @Column(name = "nama")
    private String nama;

//    @NotNull
    @Column(name = "harga")
    private int harga;

    @NotNull
    @Column(name = "stok", nullable = false)
    private int stok;

//    @NotNull
    @Size(max = 100)
    @Column(name = "kategori")
    private String kategori;

    //Relasi dengan CabangModel
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cabang_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private CabangModel cabang;

    @Column(name = "id_promo")
    private int idPromo;
}
