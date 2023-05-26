package edu.sustech.re.user;

import edu.sustech.re.system.PageUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserGroup {
    //id
    Long id;
    //user_name
    String name;
    //cost
    Integer cost;
    Integer total;
    Integer left;
    List<PageUser> users;
    List<UserFund> fund;
    Boolean isAdmin;
}
