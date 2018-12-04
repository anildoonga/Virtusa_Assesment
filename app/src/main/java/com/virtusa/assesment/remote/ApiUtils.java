package com.virtusa.assesment.remote;


import com.virtusa.assesment.interfaces.WipServiceInterface;

/**
 * Created by Anil on 2/12/2018.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ApiUtils {
    //public static final String Main_URL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/";
    public static final String Main_URL = "https://jsonplaceholder.typicode.com";
    /**
     * @return : provide an interface to our application through the getWipService()
     */
    public static WipServiceInterface getWipService() {
        return RetrofitClient.getClient().create(WipServiceInterface.class);
    }



}