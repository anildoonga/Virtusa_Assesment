package com.virtusa.assesment;

import com.virtusa.assesment.interfaces.WipServiceInterface;
import com.virtusa.assesment.model.Row;
import com.virtusa.assesment.remote.RetrofitClient;

import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ResponseTest {
    @Mock
    Row row;

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    /**
     * Test to validate Row Count
     */
    @Test
    public void rowsCountCheck() {
        List<Row> rows;
        WipServiceInterface apiEndpoints = RetrofitClient.getClient().create(WipServiceInterface.class);
        Call<List<Row>> call = apiEndpoints.getServerData();
        try {
            Response<List<Row>> response = call.execute();
            rows = response.body();
            if (!rows.isEmpty()) {
                assertEquals(100, rows.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test to validate row Description
     */
    @Test
    public void rowDescCheck() {
        List<Row> rows;
        WipServiceInterface apiEndpoints = RetrofitClient.getClient().create(WipServiceInterface.class);
        Call<List<Row>> call = apiEndpoints.getServerData();
        try {
            Response<List<Row>> response = call.execute();
            rows = response.body();
            assertEquals("sunt qui excepturi placeat culpa", rows.get(1).getTitle());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}