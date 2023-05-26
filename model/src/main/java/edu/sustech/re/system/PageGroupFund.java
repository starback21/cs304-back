package edu.sustech.re.system;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageGroupFund {
    String complete;
    String group;
    long total;
    long cost;
    long left;
    int percent;

}
