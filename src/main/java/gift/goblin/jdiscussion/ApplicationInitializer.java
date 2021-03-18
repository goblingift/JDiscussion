/*
 * Copyright (C) 2020 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.jdiscussion;

import gift.goblin.jdiscussion.bean.SessionManager;
import gift.goblin.jdiscussion.mongodb.model.UserGroup;
import gift.goblin.jdiscussion.mongodb.repo.UserGroupRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author andre
 */
@Service
public class ApplicationInitializer implements InitializingBean {

    @Autowired
    UserGroupRepository userGroupRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void afterPropertiesSet() throws Exception {

        List<UserGroup> allUserGroups = userGroupRepository.findAll();
        generateUserGroup1IfNotExists(allUserGroups);
        generateUserGroup2IfNotExists(allUserGroups);
        generateUserGroup3IfNotExists(allUserGroups);
        generateUserGroup4IfNotExists(allUserGroups);
    }

    private void generateUserGroup1IfNotExists(List<UserGroup> allUserGroups) {
        if (allUserGroups.stream().noneMatch(ug -> SessionManager.GROUP_ID_1 == ug.getId())) {
            UserGroup newUserGroup = new UserGroup(SessionManager.GROUP_ID_1, 1, SessionManager.GROUP_DESCR_1);
            userGroupRepository.save(newUserGroup);
            logger.info("UserGroup-{} ({}) was created, because it didnt exist in DB.", newUserGroup.getNumber(), newUserGroup.getDescription());
        }
    }

    private void generateUserGroup2IfNotExists(List<UserGroup> allUserGroups) {
        if (allUserGroups.stream().noneMatch(ug -> SessionManager.GROUP_ID_2 == ug.getId())) {
            UserGroup newUserGroup = new UserGroup(SessionManager.GROUP_ID_2, 2, SessionManager.GROUP_DESCR_2);
            userGroupRepository.save(newUserGroup);
            logger.info("UserGroup-{} ({}) was created, because it didnt exist in DB.", newUserGroup.getNumber(), newUserGroup.getDescription());
        }
    }

    private void generateUserGroup3IfNotExists(List<UserGroup> allUserGroups) {
        if (allUserGroups.stream().noneMatch(ug -> SessionManager.GROUP_ID_3 == ug.getId())) {
            UserGroup newUserGroup = new UserGroup(SessionManager.GROUP_ID_3, 3, SessionManager.GROUP_DESCR_3);
            userGroupRepository.save(newUserGroup);
            logger.info("UserGroup-{} ({}) was created, because it didnt exist in DB.", newUserGroup.getNumber(), newUserGroup.getDescription());
        }
    }

    private void generateUserGroup4IfNotExists(List<UserGroup> allUserGroups) {
        if (allUserGroups.stream().noneMatch(ug -> SessionManager.GROUP_ID_4 == ug.getId())) {
            UserGroup newUserGroup = new UserGroup(SessionManager.GROUP_ID_4, 4, SessionManager.GROUP_DESCR_4);
            userGroupRepository.save(newUserGroup);
            logger.info("UserGroup-{} ({}) was created, because it didnt exist in DB.", newUserGroup.getNumber(), newUserGroup.getDescription());
        }
    }

}
