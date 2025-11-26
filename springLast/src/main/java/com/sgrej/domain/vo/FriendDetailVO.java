package com.sgrej.domain.vo;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FriendDetailVO {

    //用户基本信息
    private UserVO userVO;

    //用户评论
    private List<ReviewVO> reviewsList;

    //用户收藏
    private  List<CollectionVO> collectionVOList;

    //用户好友
    private List<FindfriendVO> findfriendVOS;


}
