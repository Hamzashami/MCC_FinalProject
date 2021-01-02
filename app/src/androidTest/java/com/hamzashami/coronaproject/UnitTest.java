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

public class UnitTest {
    private final CountDownLatch latch = new CountDownLatch(4);
    JSONObject jsonObject = null;
    boolean isLogin = false;
    boolean isRegister = false;
    Validation validation;

    @Before
    public void beforeTest() {
        validation = new Validation();
    }

    @Test
    public void login() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://tasweeeg.com/api/v1/login", null, response -> {
            jsonObject = response;
            try {
                if (jsonObject.getBoolean("status")) {
                    isLogin = true;
                } else {
                    isLogin = false;
                }
                latch.countDown();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            isLogin = false;
            latch.countDown();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("mobile", "0592471020");
                hashMap.put("password", "123456");
                hashMap.put("country_code", "+970");
                return super.getParams();
            }
        };
        Volley.newRequestQueue(appContext).add(jsonObjectRequest);
        latch.await(10, TimeUnit.SECONDS);

        assertFalse(isLogin);
    }

    @Test
    public void testRegister() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://tasweeeg.com/api/v1/register", null, response -> {
            jsonObject = response;
            Toast.makeText(appContext, "hello", Toast.LENGTH_SHORT).show();

            try {
                if (jsonObject.getBoolean("status")) {
                    isRegister = true;
                } else {
                    isRegister = false;
                }
                latch.countDown();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(appContext, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            isRegister = false;
            latch.countDown();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("name", "yosefmoq");
                hashMap.put("email", "yosefmoqq@gmail.com");
                hashMap.put("mobile", "0592247102");
                hashMap.put("password", "123456");
                hashMap.put("country_code", "+970");
                return super.getParams();
            }
        };
        Volley.newRequestQueue(appContext).add(jsonObjectRequest);
        latch.await(10, TimeUnit.SECONDS);

        assertFalse(isRegister);
    }

    @Test
    public void isValidEmail() {
        assertEquals(true, validation.validateEmail("shamihamza25@gmail.com"));
    }

    @Test
    public void isValidUsername() {
        assertEquals(false, validation.validateUsername("shamihamza25"));
    }

    @Test
    public void isValidPassword() {
        assertEquals(true, validation.validatePassword("123456"));
    }

    @Test
    public void isValidConfirmPassword() {
        assertEquals(false, validation.vaidateConfirmPassword("12345678", "1234567"));
    }

    @After
    public void after() {
        validation = null;
    }

}