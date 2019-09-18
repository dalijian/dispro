package com.lijian.dispro.quartz.job;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@Component
public class WeatherCrawl extends BaseJob implements Job {
    final String url = "http://www.weather.com.cn/data/sk/";
    Logger log = LoggerFactory.getLogger(WeatherCrawl.class);

    private static Map<String, String> areaName2WeatherUrl = new HashMap<>();
    static ObjectMapper mapper = new ObjectMapper();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String tableName = "crawl_weather";

    /***
     * 可以拿到 jobDetail,JobDataMap, 等等，JobDataMap  不适合 存储大量数据，应为 JobDataMap  读取是 一次性的，
     * 更新也是 也是一次性的，随着数据的增加，更新数据 大小，会超过 mysql max_allowed_package(默认4M)
     * @param context
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<Map<String, String>> modelList = new ArrayList<>();

        areaName2WeatherUrl.forEach((k, v) -> {

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Map<String, String> result = getWeatherInfo(v);
            modelList.add(result);

        });


        saveToDatabase(context, modelList);
    }

    private void saveToDatabase(JobExecutionContext context, List<Map<String, String>> modelList) {

        String insertSql = "insert into " + tableName + "("
                + modelList.get(0).keySet().stream().collect(Collectors.joining(",")) + ")" + "values" + "(";
        for (int i = 0; i < modelList.get(0).keySet().size(); i++) {
            insertSql += "?,";
        }
        insertSql = insertSql.substring(0, insertSql.lastIndexOf(",")) + ")";

        List<Object[]> valueList = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            valueList.add(modelList.get(i).values().stream().collect(Collectors.toList()).toArray());
        }
        disproJdbcTemplate.batchUpdate(insertSql, valueList);
    }


    public Map<String, String> getWeatherInfo(String weatherCode) {
        log.info("url:{}", url + weatherCode + ".html");
        String result = getWeather(url + weatherCode + ".html");
        try {
            String utf8Result = new String(result.getBytes("ISO-8859-1"), Charset.forName("utf-8"));
            log.info(utf8Result);
            Map<String, Map<String, String>> resultMap = mapper.readValue(utf8Result, Map.class);

            Map<String, String> wetherinfoMap = resultMap.get("weatherinfo");

            return wetherinfoMap;
//            String city = resultMap.get("weatherinfo").get("SD");
//            String cityId = resultMap.get("weatherinfo").get("SD");
//            String temp = resultMap.get("weatherinfo").get("SD");
//            String wd = resultMap.get("weatherinfo").get("SD");
//            String ws = resultMap.get("weatherinfo").get("SD");
//            String cd = resultMap.get("weatherinfo").get("SD");
//            String ap = resultMap.get("weatherinfo").get("SD");
//            String njg = resultMap.get("weatherinfo").get("SD");
//            String wse = resultMap.get("weatherinfo").get("SD");
//            String time = resultMap.get("weatherinfo").get("WSE");
//            String sm = resultMap.get("weatherinfo").get("WSE");
//            String isRadar = resultMap.get("weatherinfo").get("WSE");
//            String radar = resultMap.get("weatherinfo").get("WSE");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getWeather(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
//    List<String> cookies=new ArrayList<>();
//        cookies.add("jeesite.session.id="+this.sessionValue);
        headers.add("Accept", "application/json");
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
//        headers.put(HttpHeaders.COOKIE,cookies);
        HttpEntity request = new HttpEntity(null, headers);
//        MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
        headers.setContentType(type);
        String msg = restTemplate.getForObject(url, String.class, String.class);
        return msg;
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    @PostConstruct
    public void readWeatherJson() {
        try {
            areaName2WeatherUrl = mapper.readValue(this.getClass().getClassLoader().getResourceAsStream("weatherStation.json"), Map.class);
            log.info("初始化从配置文件中读取weatherStation.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
