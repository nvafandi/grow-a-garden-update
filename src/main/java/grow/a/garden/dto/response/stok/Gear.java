package grow.a.garden.dto.response.stok;

import lombok.Data;

import java.util.List;

@Data
public class Gear {

    private List<Items> items;

    private String countdown;

}
