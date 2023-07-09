//package com.binance.feign.sample.consumer;
//
//import java.math.BigDecimal;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.binance.feign.sample.provider.service.ProviderService;
//import com.binance.feign.sample.provider.vo.Pojo;
//
//@RestController
//public class TestService {
//    private static final ExecutorService SEND_MESSAGE_EXECUTOR = Executors.newSingleThreadExecutor();
//    @Autowired
//    private ProviderService providerService;
//
//    @GetMapping("/test")
//    public Pojo test(HttpServletRequest request) {
//        String test = request.getParameter("test");
//        System.out.print(test);
//        Pojo pojo = new Pojo();
//        pojo.setName("charles");
//        pojo.setValue(BigDecimal.valueOf(120.122323232323));
//        providerService.hello();
//        return pojo;
//    }
//
//}