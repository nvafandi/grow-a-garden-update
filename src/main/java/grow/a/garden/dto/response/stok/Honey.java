package grow.a.garden.dto.response.stok;

import lombok.Data;

import java.util.List;

@Data
public class Honey {

    private List<Items> items;

    private String countdown;

}
