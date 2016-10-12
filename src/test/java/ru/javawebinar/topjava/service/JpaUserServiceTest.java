package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

/**
 * Created by admin on 10/11/2016.
 */

@ActiveProfiles(Profiles.JPA)
public class JpaUserServiceTest extends UserServiceTest {
}
