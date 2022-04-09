package com.fishedee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnumDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Constant{
        private String label;

        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnumInfo{
        private String name;

        private String packageName;

        private String encloseName;

        public List<Constant> constantList = new ArrayList<>();
    }

    private int code;

    private String msg;

    private List<EnumInfo> data = new ArrayList<>();
}
