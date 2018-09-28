package com.ecnu.leon.jarvis.model.reading.model;

/**
 * Created by LeonDu on 14/09/2018.
 */

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookMark {
    private int reviewLevel;
    private int startPage;
    private int endPage;
}
