package com.example.service.test;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.dao.GroupDao;
import com.example.dao.StudentDao;
import com.example.TestConfig;
import com.example.model.Group;
import com.example.model.Student;
import com.example.service.Data;
import com.example.service.GroupService;
import com.example.service.StudentService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("Test")
class GroupServiceTest {

  @Value("${groups}")
  private int groupsTest;
  @MockBean
  GroupDao groupDao;
  @MockBean
  StudentDao studentDao;
  @MockBean
  StudentService studentService;
  @MockBean
  Data data;
  @Autowired
  GroupService groupService;

  @Test
  void createData_ShouldAddedSetQuantityGroupsToDb() {

    groupService.createData();
    List<Group> list = new ArrayList<>();
    for (int i = 0; i < groupsTest; i++) {
      list.add(null);
    }
    Mockito.when(groupDao.getAll()).thenReturn(list);
    Assertions.assertEquals(groupsTest, groupService.getAll().size());
  }

  @Test
  void getGroupsByStudentCount_ShouldCallGroupDaoMethodGetGroupsByStudentCount() {
    int num = 3;
    groupService.getGroupsByStudentCount(num);
    verify(groupDao, times(1)).getGroupsByStudentCount(num);
  }

  @Test
  void getAll_ShouldCallGroupDaoMethodGetAll() {
    groupService.getAll();
    verify(groupDao, times(1)).getAll();
  }
}
