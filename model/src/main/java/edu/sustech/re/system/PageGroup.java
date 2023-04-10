package edu.sustech.re.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于返回分页查询中的user
 *与前端数据一致
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageGroup {
    //id
    Long id;
    //user_name
    String name;
    //cost
    Integer cost;
    Integer total;
    Integer left;
    List<PageUser> users;
}
