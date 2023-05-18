package edu.sustech.re.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageMsg {
    String type;
    Date date;
    Boolean newComing;
    String msg;
}
