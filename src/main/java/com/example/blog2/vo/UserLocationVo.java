package com.example.blog2.vo;

import lombok.Data;

/**
 * Auto-generated: 2023-01-29 12:23:2
 * @author mxp
 */
@Data
public class UserLocationVo {

    private Integer status;
    private String message;
    private Result result;

    @Data
    public static class Result {
        private String ip;
        private Location location;
        private Ad_info ad_info;
    }

    @Data
    public static class Location {
        private Double lat;
        private Double lng;
    }

    @Data
    public static class Ad_info {
        private String nation;
        private String province;
        private String city;
        private String district;
        private Long adcode;
    }
}
