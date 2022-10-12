package org.mobon.billing.kafka.stream;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostgreSQLRunner implements ApplicationRunner {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try(Connection connection = dataSource.getConnection()){
            System.out.println(connection);
            
            String URL = connection.getMetaData().getURL();
            System.out.println(URL);
            
            String User = connection.getMetaData().getUserName();
            System.out.println(User);

//            Statement statement = connection.createStatement();
//            String sql = "CREATE TABLE ACCOUNT(" +
//                    "ID INTEGER NOT NULL," +
//                    "NAME VARCHAR(255)," +
//                    "PRIMARY KEY(ID))";
//            statement.executeUpdate(sql);
        }

        jdbcTemplate.execute("insert into newtable (stats_dttm, tp_code, cnt) VALUES(20210312, '01', 1)");
    }
}
