package com.example.service;

import com.example.FileReader;
import com.example.dao.CourseDao;
import com.example.dao.GroupDao;
import com.example.dao.StudentDao;
import com.example.model.Course;
import com.example.model.Group;
import com.example.model.Student;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:generation.properties")
public class StudentService {

  @Value("${first-name-file}")
  private String fileFirstName;
  @Value("${last-name-file}")
  private String fileLastName;
  @Value("${number-student-min}")
  private String numberStudentMin;
  @Value("${number-student-max}")
  private String numberStudentMax;
  @Value("${students-courses-min}")
  private String studentsCoursesMin;
  @Value("${students-courses-max}")
  private String studentsCoursesMax;
  @Value("${students-total}")
  private String studentsTotalNumber;
  @Value("${courses}")
  private String coursesNumber;
  private FileReader fileReader;
  private CourseDao courseDao;
  private GroupDao groupDao;
  private StudentDao studentDao;
  private Random random;
  private int studentsWithGroup;

  @Autowired
  public StudentService(FileReader fileReader, CourseDao courseDao, GroupDao groupDao,
      StudentDao studentDao, Random random) {
    this.fileReader = fileReader;
    this.courseDao = courseDao;
    this.groupDao = groupDao;
    this.studentDao = studentDao;
    this.random = random;
  }

  public void createData() throws IOException, URISyntaxException {
    if (groupDao.getAll().isEmpty()) {
      throw new IllegalArgumentException("No data available from group!!!");
    }
    if (courseDao.getAll().isEmpty()) {
      throw new IllegalArgumentException("No data available from course!!!");
    } else {
      studentDao.add(createStudentsList());
      studentDao.addStudentsCourse(createStudentsCourseList());
    }
  }


  private List<Student> createStudentsList() throws IOException, URISyntaxException {
    String[] firstNames = fileReader.readFile(fileFirstName).toArray(new String[]{});
    String[] lastNames = fileReader.readFile(fileLastName).toArray(new String[]{});
    Set<Integer> set = setGroupId();
    List<Student> studentListWithGroup = createStudentListWithGroup(set, firstNames, lastNames);
    List<Student> studentListWithoutGroup = createStudentListWithoutGroup(firstNames, lastNames);

    return Stream.concat(studentListWithGroup.stream(), studentListWithoutGroup.stream()).toList();
  }

  private Set<Integer> setGroupId() {
    List<Integer> groupsId = new ArrayList<>();
    for (Group group : groupDao.getAll()) {
      groupsId.add(group.getId());
    }
    Set<Integer> groupsIdSet = new HashSet<>();
    for (int i = 0; i < groupsId.size(); i++) {
      groupsIdSet.add(groupsId.get((randomInt(random, 1, groupsId.size())) - 1));
    }
    return groupsIdSet;
  }

  private int randomInt(Random random, int origin, int bound) {
    if (origin >= bound) {
      throw new IllegalArgumentException();
    }
    return origin + random.nextInt(bound);
  }

  private List<Student> createStudentListWithGroup(Set<Integer> set, String[] firstNames,
      String[] lastNames) {
    studentsWithGroup = 0;
    int studTotal = Integer.valueOf(studentsTotalNumber);
    List<Student> studentList = new ArrayList<>();

    for (Integer d : set) {
      int number = randomInt(random, Integer.valueOf(numberStudentMin),
          Integer.valueOf(numberStudentMax));
      int i = 0;

      while (studentsWithGroup < studTotal && i <= number) {
        Student student = new Student();
        student.setGroupId(d);
        student.setFirstName(firstNames[randomInt(random, 0, firstNames.length - 1)]);
        student.setLastName(lastNames[randomInt(random, 0, lastNames.length - 1)]);
        for (Group group : groupDao.getAll()) {
          if (group.getId() == d) {
            student.setGroupName(group.getName());
          }
        }
        studentList.add(student);
        studentsWithGroup++;
        i++;
      }
    }
    return studentList;
  }

  private List<Student> createStudentListWithoutGroup(String[] firstNames, String[] lastNames) {
    int studTotal = Integer.valueOf(studentsTotalNumber);
    List<Student> studentList = new ArrayList<>();
    for (int i = 0; i < studTotal - studentsWithGroup; i++) {
      Student student = new Student();
      student.setFirstName(firstNames[randomInt(random, 0, firstNames.length - 1)]);
      student.setLastName(lastNames[randomInt(random, 0, lastNames.length - 1)]);
      studentList.add(student);
    }
    return studentList;
  }

  private List<Student> createStudentsCourseList() {
    List<Course> courseList = courseDao.getAll();
    BitSet bitSet = new BitSet(Integer.valueOf(coursesNumber));
    List<Student> studentListNew = new ArrayList<>();

    for (Student student : studentDao.getAll()) {
      List<Course> courseListAfterAdditionCourses = new ArrayList<>();
      int k = randomInt(random, Integer.valueOf(studentsCoursesMin),
          Integer.valueOf(studentsCoursesMax));
      int i = 0;
      bitSet.clear();

      while (i < k) {
        int course = randomInt(random, 1, Integer.valueOf(coursesNumber));
        if (!bitSet.get(course)) {
          bitSet.set(course);
          i++;
          courseListAfterAdditionCourses.add(courseList.get(course - 1));
        }

      }
      student.setCourse(courseListAfterAdditionCourses);
      studentListNew.add(student);
    }
    return studentListNew;
  }

  public List<Student> getWithCourse() {
    return studentDao.getWithCourse();
  }

  public List<Student> getWithOutCourse(int courseId) {
    return studentDao.getWithOutCourse(courseId);
  }

  public List<Student> getAll() {
    return studentDao.getAll();
  }

  public void add(Student student) {
    studentDao.add(student);
  }

  public void add(List<Student> student) {
    studentDao.add(student);
  }

  public void delete(int id) {
    studentDao.delete(id);
  }

  public void deleteFromCourse(int studentId, int courseId) {
    studentDao.deleteFromCourse(studentId, courseId);
  }

  public void addStudentsCourse(int studId, int courseNumber) {
    studentDao.addStudentsCourse(studId, courseNumber);
  }

  public void clear() {
    studentDao.clearAll();
  }
}