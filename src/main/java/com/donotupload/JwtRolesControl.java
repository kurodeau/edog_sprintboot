package com.donotupload;

import java.util.Arrays;
import java.util.List;

import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSourceType;

public enum JwtRolesControl {
	ROLE_MANAGERJWTV2(Arrays.asList("/back/api/nouse")),
	ROLE_MANAGERJWT(Arrays.asList("/back/api/seller")),
	ROLE_MANAGER(Arrays.asList("/back/api/seller")),
    ROLE_BUYER(Arrays.asList("/api/buyer", "/api/buyer/users"));

    private final List<String> apiPaths;


    JwtRolesControl(List<String> apiPaths) {
        this.apiPaths = apiPaths;
    }

    public List<String> getApiPaths() {
        return apiPaths;
    }


    public static List<String> getApiAccessPaths(JwtRolesControl role) {
        return role.getApiPaths();
    }

//    public static void main(String[] args) {
//        List<String> managerApis = getApiAccessPaths(JwtRolesControl.ROLE_MANAGERJWTV2);
//        System.out.println("ROLE_MANAGER APIs: " + managerApis);
////        System.out.println("ROLE_MANAGER APIs: " + JwtRolesControl.valueOf());
//        System.out.println(JwtRolesControl.getApiAccessPaths(JwtRolesControl.valueOf("ROLE_MANAGERJWTV2")));
//        List<String> adminApis = getApiAccessPaths(JwtRolesControl.ROLE_BUYER);
//        System.out.println("ROLE_ADMIN APIs: " + adminApis);
//    }
}
