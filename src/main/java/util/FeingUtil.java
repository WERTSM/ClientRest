package util;

import api.IRestController;
import feign.Feign;
import feign.okhttp.OkHttpClient;
import okhttp3.JavaNetCookieJar;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Collections;

public class FeingUtil {

    private static JavaNetCookieJar instanceCookieJar;

    @NotNull
    final private static String URL_BASE = "http://localhost:9999/rest/";

    public static <T extends IRestController> T getInstance(Class<T> restController) {
        final FormHttpMessageConverter converter = new FormHttpMessageConverter();
        final HttpMessageConverters converters = new HttpMessageConverters(converter);
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        final ObjectFactory<HttpMessageConverters> objectFactory = () -> converters;
        final CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        final okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient().newBuilder();
        builder.cookieJar(getInstanceCookieJar(cookieManager));
        return Feign.builder()
                .client(new OkHttpClient(builder.build()))
                .contract(new SpringMvcContract())
                .encoder(new SpringEncoder(objectFactory))
                .decoder(new SpringDecoder(objectFactory))
                .target(restController, URL_BASE);
    }

    private static JavaNetCookieJar getInstanceCookieJar(CookieManager cookieManager) {
        if (instanceCookieJar == null) {
            instanceCookieJar = new JavaNetCookieJar(cookieManager);
            return instanceCookieJar;
        } else {
            return instanceCookieJar;
        }
    }
}