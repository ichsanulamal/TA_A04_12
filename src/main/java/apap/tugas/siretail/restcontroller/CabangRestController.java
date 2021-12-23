package apap.tugas.siretail.restcontroller;

import apap.tugas.siretail.model.CabangModel;
import apap.tugas.siretail.service.CabangRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CabangRestController {
    @Autowired
    private CabangRestService cabangRestService;

    @GetMapping(path = "/cabang/{id}", produces = {"application/json"})
    public ResponseEntity getCabangById(@PathVariable("id") int id) {
        CabangModel cabang = cabangRestService.getCabangById(id);
        return cabang != null ? ResponseEntity.ok(cabang) : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/cabang/", produces = {"application/json"})
    public ResponseEntity getCabangById() {
        List<CabangModel> listCabang = cabangRestService.getListCabang();
        return listCabang != null ? ResponseEntity.ok(listCabang) : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path="/cabang/request")
    public CabangModel createCabang(@Valid @RequestBody CabangModel cabang, BindingResult bindingResult) {
        
        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Request body has invalid or missing field"
            );

        } else {
            cabang.setStatus(0);
            return cabangRestService.requestCreateCabang(cabang);
        }
    }

}
