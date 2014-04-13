/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.server.application.security;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import javax.annotation.PostConstruct;
import org.apache.commons.codec.binary.Base64;
import org.housecream.server.api.domain.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/*
 * important JVM arg : -Djava.security.egd=file:/dev/./urandom
 */
@Component
public class RandomStringGenerator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final SecureRandom random;

    @Autowired
    private Config props;

    @PostConstruct
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void seed() {
        logger.debug("Starting reseed.");
        random.setSeed(random.generateSeed(props.getSecuritySeedLength()));
        logger.debug("Finished reseed.");
    }

    public RandomStringGenerator() {
        try {
            random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new IllegalStateException("failed to create a SecureRandom instance", e);
        }
    }

    public String numeric(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length < 1: " + length);
        }
        StringBuffer buffer = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            buffer.append(random.nextInt(9));
        }
        return buffer.toString();
    }

    public String base64(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length < 1: " + length);
        }
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return new String(Base64.encodeBase64(bytes)).substring(0, length);
    }
}
