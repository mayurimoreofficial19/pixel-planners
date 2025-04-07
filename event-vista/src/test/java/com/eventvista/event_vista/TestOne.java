//package com.eventvista.event_vista;
//
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class TestOne extends AbstractTest{
//
//    //check application.properties file for database connection properties
//@Test
//    public void testDBConnectionProperties() throws IOException{
//    String propertiesFileContents = getFileContent("src/main/resources/application.properties");
//
//    Pattern urlPattern = Pattern.compile("spring.datasource.url=jdbc:mysql://localhost:3306/eventvista");
//    Matcher urlMatcher = urlPattern.matcher(propertiesFileContents);
//    boolean urlFound = urlMatcher.find();
//    assertTrue(urlFound, "Database connection URL not found or is incorrect");
//
//    Pattern usernamePattern = Pattern.compile("spring.datasource.username=eventvista");
//    Matcher usernameMatcher = usernamePattern.matcher(propertiesFileContents);
//    boolean usernameFound = usernameMatcher.find();
//    assertTrue(usernameFound, "Database username not found or is incorrect");
//
//    Pattern passwordPattern = Pattern.compile("spring.datasource.password=eventvista");
//    Matcher passwordMatcher = passwordPattern.matcher(propertiesFileContents);
//    boolean passwordFound = passwordMatcher.find();
//    assertTrue(passwordFound, "Database password not found or is incorrect");
//
//}
//
//    @Test
//    public void testDbGradleDependencies () throws IOException {
//        String gradleFileContents = getFileContent("build.gradle");
//
//        Pattern jpaPattern = Pattern.compile("org.springframework.boot:spring-boot-starter-data-jpa");
//        Matcher jpaMatcher = jpaPattern.matcher(gradleFileContents);
//        boolean jpaFound = jpaMatcher.find();
//        assertTrue(jpaFound, "JPA dependency not found or is incorrect");
//
//        Pattern mysqlPattern = Pattern.compile("mysql:mysql-connector-java");
//        Matcher mysqlMatcher = mysqlPattern.matcher(gradleFileContents);
//        boolean mysqlFound = mysqlMatcher.find();
//        assertTrue(mysqlFound, "MySQL dependency not found or is incorrect");
//
//    }
//}
