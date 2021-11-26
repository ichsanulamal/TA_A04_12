package apap.tugas.siretail.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ListSiItemModel {
    private List<SiItemModel> listSiItem;
}
