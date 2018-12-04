package com.virtusa.assesment.interfaces;

import com.virtusa.assesment.model.Row;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * WipServiceInterface defines HTTP operations
 */
public interface WipServiceInterface {
    /**
     * @return :Api Endpoints providing callback to enqueue
     * The return value wraps the response in a Call object with the type of the expected(Row) result.
     */
    @SuppressWarnings("JavaDoc")
    @GET("/albums")
    Call <List<Row>> getServerData();
 }