package grow.a.garden.controller;

import grow.a.garden.service.ItemsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/syncItems")
    public ResponseEntity<Object> syncItems() {
        var response = itemsService.syncItems();

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @PutMapping("/updateIcon")
    public ResponseEntity<Object> put(
            @RequestParam("itemId") String itemId,
            @RequestParam("iconUrl") String icon) {
        var response = itemsService.updateItemIcon(itemId, icon);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

}
