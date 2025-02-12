package com.users.auth_api.component;

import com.users.auth_api.util.AdminUserInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializerRunner implements CommandLineRunner{

    private final AdminUserInitializer adminUserInitializer;

    @Override
    public void run(String... args) throws Exception {
        adminUserInitializer.initializeAdminUser();
    }

}
