package com.example.bookitdriver.Common;

import com.example.bookitdriver.pojo.IGoogleApI;
import com.example.bookitdriver.pojo.RetrofitClient;



public class Common {
    public static final String baseURL ="https://maps.googleapis.com";
    public  static IGoogleApI getGoogleApI()
    {
        return RetrofitClient.getClent(baseURL).create(IGoogleApI.class);

        }

}
