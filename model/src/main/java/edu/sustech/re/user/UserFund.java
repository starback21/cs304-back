package edu.sustech.re.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserFund {

    Long id;
    String name;
    int remain_amount;
    String category;
}
