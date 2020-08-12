package org.springframework.data.redis.core;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * redis布隆过滤器命令
 * @author duzou
 */

@Getter
@AllArgsConstructor
public enum BloomCommand {

    RESERVE("BF.RESERVE"),
    ADD("BF.ADD"),
    MADD("BF.MADD"),
    EXISTS("BF.EXISTS"),
    MEXISTS("BF.MEXISTS");

    private String command;

}
