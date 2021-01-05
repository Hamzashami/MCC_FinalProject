package com.hamzashami.coronaproject;

import android.content.Context;
import android.widget.Toast;

import androidx.test.platform.app.InstrumentationRegistry;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnitTest {
    private final CountDownLatch downLatch = new CountDownLatch(3);
    JSONObject jsonObject = null;
    boolean logined = false;
    boolean registered = false;
    isChecked isValed;
    String url ="https://www.countryflags.io/";

    @Before
    public void beforeTest() {
        isValed = new isChecked();
    }

    @Test
    public void login() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            jsonObject = response;
            try {
                if (jsonObject.getBoolean("status")) {
                    logined = true;
                } else {
                    logined = false;
                }
                downLatch.countDown();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            logined = false;
            downLatch.countDown();
        }) {
        };
        Volley.newRequestQueue(appContext).add(jsonObjectRequest);
        downLatch.await(15, TimeUnit.SECONDS);

        assertFalse(logined);
    }

    @Test
    public void testRegister() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            jsonObject = response;
            Toast.makeText(appContext, "Hi!", Toast.LENGTH_SHORT).show();

            try {
                if (jsonObject.getBoolean("status")) {
                    registered = true;
                } else {
                    registered = false;
                }
                downLatch.countDown();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(appContext, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            registered = false;
            downLatch.countDown();
        }) {

        };
        Volley.newRequestQueue(appContext).add(jsonObjectRequest);
        downLatch.await(15, TimeUnit.SECONDS);

        assertFalse(registered);
    }

    @Test
    public void isValidEmail() {
        assertEquals(true, isValed.validateEmail("shamihamza25@gmail.com"));
    }

    @Test
    public void isValidUsername() {
        assertEquals(true, isValed.validateUsername("shamihamza25"));
    }

    @Test
    public void isValidPassword() {
        assertEquals(false, isValed.validatePassword("123456"));
    }

    @Test
    public void isValidConfirmPassword() {
        assertEquals(false, isValed.vaidateConfirmPassword("123456", "1234567"));
    }

    @After
    public void after() {
        isValed = null;
    }

}