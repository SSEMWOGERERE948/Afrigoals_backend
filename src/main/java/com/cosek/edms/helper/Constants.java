package com.cosek.edms.helper;

public class Constants {
    public static final String GENERAL_ROUTE = "/api/v1";

    // User permissions
    public static final String READ_USER = "READ_USER";
    public static final String CREATE_USER = "CREATE_USER";
    public static final String DELETE_USER = "DELETE_USER";
    public static final String UPDATE_USER = "UPDATE_USER";

    // Role permissions
    public static final String READ_ROLE = "READ_ROLE";
    public static final String CREATE_ROLE = "CREATE_ROLE";
    public static final String DELETE_ROLE = "DELETE_ROLE";
    public static final String UPDATE_ROLE = "UPDATE_ROLE";

    // Permission management permissions
    public static final String READ_PERMISSION = "READ_PERMISSION";
    public static final String CREATE_PERMISSION = "CREATE_PERMISSION";
    public static final String DELETE_PERMISSION = "DELETE_PERMISSION";
    public static final String UPDATE_PERMISSION = "UPDATE_PERMISSION";

    // New Dashboard permissions
    public static final String READ_DASHBOARD = "READ_DASHBOARD";
    public static final String CREATE_DASHBOARD = "CREATE_DASHBOARD";
    public static final String UPDATE_DASHBOARD = "UPDATE_DASHBOARD";
    public static final String DELETE_DASHBOARD = "DELETE_DASHBOARD";

    // New Files permissions
    public static final String READ_FILES = "READ_FILES";
    public static final String CREATE_FILES = "CREATE_FILES";
    public static final String UPDATE_FILES = "UPDATE_FILES";
    public static final String DELETE_FILES = "DELETE_FILES";

    // New Folders permissions
    public static final String READ_FOLDERS = "READ_FOLDERS";
    public static final String CREATE_FOLDERS = "CREATE_FOLDERS";
    public static final String UPDATE_FOLDERS = "UPDATE_FOLDERS";
    public static final String DELETE_FOLDERS = "DELETE_FOLDERS";

    // New Case Studies permissions
    public static final String READ_FILECATEGORY = "READ_FILECATEGORY";
    public static final String CREATE_FILECATEGORY = "CREATE_FILECATEGORY";
    public static final String UPDATE_FILECATEGORY = "UPDATE_FILECATEGORY";
    public static final String DELETE_FILECATEGORY = "DELETE_FILECATEGORY";

    public static final String READ_REQUESTS = "READ_REQUESTS";
    public static final String CREATE_REQUESTS = "CREATE_REQUESTS";
    public static final String UPDATE_REQUESTS = "UPDATE_REQUESTS";
    public static final String DELETE_REQUESTS = "DELETE_REQUESTS";

    public static final String READ_DEPARTMENTS = "READ_DEPARTMENTS";
    public static final String CREATE_DEPARTMENTS = "CREATE_DEPARTMENTS";
    public static final String UPDATE_DEPARTMENTS = "UPDATE_DEPARTMENTS";
    public static final String DELETE_DEPARTMENTS = "DELETE_DEPARTMENTS";

    // Other constants...
    public static final String USER_ROUTE = GENERAL_ROUTE + "/**";
    public static final String ROLE_ROUTE = GENERAL_ROUTE + "/roles/**";
    public static final String PERMISSION_ROUTE = GENERAL_ROUTE + "/permissions/**";
    public static final String AUTH_ROUTE = GENERAL_ROUTE + "/auth/**";

    public static final String SUPER_ADMIN = "SUPER_ADMIN";
    public static final String ADMIN_EMAIL = "admin@example.com";
    public static final String ADMIN_PASSWORD = "password123";
    public static final String ADMIN_PHONE = "+256777338787";
    public static final String ADMIN_FIRST_NAME = "Admin";
    public static final String ADMIN_LAST_NAME = "Super";
    public static final String ADMIN_COUNTRY = "Uganda";
    public static final String MANAGER = "MANAGER";


    public static final String SUCCESSFUL_DELETION = "Delete Successful";
    public static final String FAILED_DELETION = "Delete Failed";
}
