package edu.sustech.re.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageApplication {
    int id;
    String name;
    String state;
    String group;
    int num;
    Date date;
    String people;
    String category;
}
