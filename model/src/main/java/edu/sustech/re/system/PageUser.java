package edu.sustech.re.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于返回分页查询中的user
 *与前端数据一致
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageUser {
    //uid
    Long key;
    //user_name
    String name;
    //group_id
    Long group;
    //mail
    String email;
    String phone;
    Boolean admin = false;
}
