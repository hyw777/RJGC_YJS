package com.sgrej.domain.vo;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FriendRecentVO {
    //朋友们最近都评价了些什么
    private List<ReviewVO> reviewVOS;

    //朋友们都收藏了些什么
    private List<CollectionVO> collectionVOS;
}
