package com.example.layer.dao;

import com.example.mapper.GroupMapper;
import com.example.model.Group;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class GroupDao {

  private final JdbcTemplate jdbcTemplate;
  private final GroupMapper groupMapper;

  @Autowired
  public GroupDao(JdbcTemplate jdbcTemplate, GroupMapper groupMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.groupMapper = groupMapper;

  }

  public void add(Group group) {
    jdbcTemplate.update("INSERT INTO groups (group_name) VALUES (?);", group.getName());
  }

  public void add(List<Group> groupList) {
    jdbcTemplate.batchUpdate("INSERT INTO groups (group_name) VALUES (?);",
        new BatchPreparedStatementSetter() {
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, groupList.get(i).getName());
          }

          public int getBatchSize() {
            return groupList.size();
          }
        }
    );
  }

  public List<Group> getAll() {

    String sql = "SELECT group_id,  group_name, number_student "
        + "FROM(SELECT groups.group_name AS group_name, groups.group_id, "
        + "COUNT (students.group_id) AS number_student "
        + "FROM groups LEFT JOIN students ON students.group_id = groups.group_id "
        + "GROUP BY groups.group_id, groups.group_name ) AS stud ORDER BY group_id;";

    return jdbcTemplate.query(sql, groupMapper);
  }

  public List<Group> getGroupsByStudentCount(int number) {

    String sql = "SELECT group_id,  group_name, number_student "
        + "FROM(SELECT groups.group_name AS group_name, groups.group_id, "
        + "COUNT (students.group_id) AS number_student "
        + "FROM groups LEFT JOIN students ON students.group_id = groups.group_id "
        + "GROUP BY groups.group_id, groups.group_name ) AS stud WHERE number_student <= ? "
        + "ORDER BY number_student;";
    return jdbcTemplate.query(sql, groupMapper, number);
  }
  public void clearAll() {
    jdbcTemplate.update("TRUNCATE  groups RESTART IDENTITY");
  }
}
