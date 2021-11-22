package apap.tugas.siretail.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "cabang")
public class CabangModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(max=30)
    @Column(name="nama", nullable = false)
    private String nama;

    @NotNull
    @Size(max=100)
    @Column(name="alamat", nullable = false)
    private String alamat;

    @NotNull
    @Column(name = "ukuran", nullable = false)
    private int ukuran;

    @NotNull
    @Column(name = "status", nullable = false)
    private int status;

    @NotNull
    @Size(max=20)
    @Column(name="no_telp", nullable = false)
    private String noTelepon;

    @ManyToOne
    @JoinColumn(
            name = "penanggung_jawab"
    )
    UserModel penanggungJawab;

    @OneToMany(mappedBy = "cabang")
    private List<ItemCabangModel> listItem;
}

