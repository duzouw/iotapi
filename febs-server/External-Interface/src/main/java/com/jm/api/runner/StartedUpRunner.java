package com.jm.api.runner;


import com.jm.api.manage.mapper.SimMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.BloomOperations;
import org.springframework.stereotype.Component;

/**
 * @author MrBird
 */
@Component
@RequiredArgsConstructor
public class StartedUpRunner implements ApplicationRunner {

    private final ConfigurableApplicationContext context;
    private final BloomOperations bloomOperations;
    private final SimMapper simMapper;

    @Override
    public void run(ApplicationArguments args) {
        if (context.isActive()) {

//            boolean iccid = bloomOperations.hasKey(BloomFilter.ICCID_FILTER);
//            boolean imsi = bloomOperations.hasKey(BloomFilter.IMSI_FILTER);
//            boolean msisdn = bloomOperations.hasKey(BloomFilter.MSISDN_FILTER);
//
//            if (!iccid || !imsi || !msisdn) {
//
//                List<String> ls = new ArrayList<>();
//                List<String> ls1 = new ArrayList<>();
//                List<String> ls2 = new ArrayList<>();
//
//                simMapper.selectList(null).forEach(x -> {
//                    ls.add(x.getIccid());
//                    ls1.add(x.getImsi());
//                    ls2.add(x.getMsisdn());
//                });
//
//                if (!iccid) {
//                    bloomOperations.createFilter(BloomFilter.ICCID_FILTER, 0.00001, 2000000);
//                    bloomOperations.addMulti(BloomFilter.ICCID_FILTER, ls);
//                }
//
//                if (!imsi) {
//                    bloomOperations.createFilter(BloomFilter.IMSI_FILTER, 0.00001, 2000000);
//                    bloomOperations.addMulti(BloomFilter.IMSI_FILTER, ls1);
//                }
//
//                if (!msisdn) {
//                    bloomOperations.createFilter(BloomFilter.MSISDN_FILTER, 0.00001, 2000000);
//                    bloomOperations.addMulti(BloomFilter.MSISDN_FILTER, ls2);
//                }
//
//            }

        }
    }
}
