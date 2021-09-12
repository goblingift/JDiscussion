/*
 * Copyright (C) 2021 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion.bean;

import gift.goblin.jdiscussion.WebSecurityConfig;
import static gift.goblin.jdiscussion.WebSecurityConfig.SESSION_FIELD_GROUPNUMBER;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Contains several methods on the session.
 *
 * @author andre
 */
@Service
public class SessionManager {

    public static final long GROUP_ID_1 = 56184684165151L;
    public static final long GROUP_ID_2 = 56154156165199L;
    public static final long GROUP_ID_3 = 56184510500054L;
    public static final long GROUP_ID_4 = 56184653210102L;
    public static final long GROUP_ID_ADMIN = 56184653219999L;
    public static final List<Long> GROUP_IDS = List.of(GROUP_ID_1, GROUP_ID_2, GROUP_ID_3, GROUP_ID_4);
    public static final String GROUP_DESCR_1 = "Gruppe 1";
    public static final String GROUP_DESCR_2 = "Gruppe 2";
    public static final String GROUP_DESCR_3 = "Gruppe 3";
    public static final String GROUP_DESCR_4 = "Gruppe 4";
    public static final String GROUP_DESCR_ADMIN = "Lehrer";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Optional<Integer> tryToGetGroupNumber(HttpSession session) {
        
        Optional<Integer> optGroupNumber = Optional.empty();

        Object attribute = session.getAttribute(SESSION_FIELD_GROUPNUMBER);
        if (attribute != null) {
            try {
                Integer groupNumber = (Integer) attribute;
                optGroupNumber = Optional.of(groupNumber);
            } catch (Exception e) {
                logger.warn("Couldnt parse the group-number in the session: {}", attribute);
            }
        }

        return optGroupNumber;
    }

    public String getGroupName(Integer groupId) {
        switch (groupId) {
            case 1:
                return GROUP_DESCR_1;
            case 2:
                return GROUP_DESCR_2;
            case 3:
                return GROUP_DESCR_3;
            case 4:
                return GROUP_DESCR_4;
        }

        return "GROUP_NOT_FOUND";
    }
    
        public Long getGroupId(Integer groupId) {
        switch (groupId) {
            case 1:
                return GROUP_ID_1;
            case 2:
                return GROUP_ID_2;
            case 3:
                return GROUP_ID_3;
            case 4:
                return GROUP_ID_4;
        }

        return 99L;
    }

    public boolean isUserAdmin(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(WebSecurityConfig.ROLE_PREFIX + WebSecurityConfig.ROLE_ADMIN))) {
            return true;
        } else {
            return false;
        }
    }

}
