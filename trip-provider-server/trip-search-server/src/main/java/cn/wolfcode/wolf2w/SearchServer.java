package cn.wolfcode.wolf2w;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SearchServer {

    public static void main(String[] args) {
        SpringApplication.run(SearchServer.class, args);
    }
}
