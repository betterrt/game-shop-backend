package com.ryan.gameshop.entity.response;

import com.ryan.gameshop.entity.Game;
import lombok.*;

import java.util.*;

@Builder
@Data
@ToString
public class PageResponse {
   private Integer totalPages;
   private Long totalElements;
   private List<?> dataList;
}
