package grow.a.garden.controller;

import grow.a.garden.service.ItemsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ItemsController {

    private final ItemsService itemsService;

    public ItemsController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @GetMapping("/items")
    public ResponseEntity<Object> items() {
        var response = itemsService.getAllItems();

        return ResponseEntity.
                status(response.getStatus())
                .body(response);
    }

    @PostMapping("/saveItems")
    public ResponseEntity<Object> saveItems() {
        var response = itemsService.syncItems();

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

}
