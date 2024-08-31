package org.boot.growup.product.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.product.persist.entity.Search;

@Data
@Builder
public class GetFavoriteKeywordResponseDTO {
    private String keyword;
    private int searchCount;

    public static GetFavoriteKeywordResponseDTO from(Search search) {
        return GetFavoriteKeywordResponseDTO.builder()
                .keyword(search.getKeyword())
                .searchCount(search.getSearchedCount())
                .build();
    }
}
