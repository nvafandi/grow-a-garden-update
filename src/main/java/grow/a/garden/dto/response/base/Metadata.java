package grow.a.garden.dto.response.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Metadata {

    @Setter
    private int page;
    @Getter
    private int limit;
    @Getter
    private int totalPage;
    @Getter
    @Setter
    private Long totalData;

    public Metadata(int page, int limit, int totalPage, Long totalData) {
        this.page = page;
        this.limit = limit;
        this.totalPage = totalPage;
        this.totalData = totalData;
    }

    public int getPage() {
        return this.page + 1;
    }


}
