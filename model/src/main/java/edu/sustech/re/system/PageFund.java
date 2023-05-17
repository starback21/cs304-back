package edu.sustech.re.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageFund {
    Long key;
    Long id;
    String name;
    Map<Integer,Integer> dataRange;
    long totalNum;
    long leftNum;
    int percent;
    String state;
    int leftDay;
    int disabled;
}
