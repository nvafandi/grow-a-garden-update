package grow.a.garden.controller;

import grow.a.garden.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/stock")
    ResponseEntity<Object> stock() {
        var response = stockService.getStock();

        return ResponseEntity.ok
                (response);
    }

    @PostMapping("/send")
    public ResponseEntity<Object> send() {

        var response = stockService.sendMessage();

        return ResponseEntity.ok(response);
    }
}
