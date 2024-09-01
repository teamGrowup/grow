package org.boot.growup.order.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.Random;

public class ExchangeIdGenerator implements BeforeExecutionGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session,
                           Object owner,
                           Object currentValue,
                           EventType eventType) {

        // 1. 현재 날짜와 시간을 yyMMddHHss 형식으로 포맷팅
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHss");
        String dateTime = dateFormat.format(new Date());

        // 2. 8자리 무작위 숫자 생성
        Random random = new Random();
        int randomNumber = random.nextInt(100000000); // 0 ~ 99999999 범위의 숫자 생성
        String formattedRandomNumber = String.format("%08d", randomNumber); // 8자리로 포맷팅

        // 3. E + 날짜와 무작위 숫자를 조합하여 주문번호 생성
        return "E" + dateTime + formattedRandomNumber;
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EventTypeSets.INSERT_ONLY;
    }
}
