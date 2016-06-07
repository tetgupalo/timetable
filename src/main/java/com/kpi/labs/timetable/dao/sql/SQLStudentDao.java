package com.kpi.labs.timetable.dao.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.kpi.labs.timetable.dao.GraduateDAO;
import com.kpi.labs.timetable.dao.GroupDAO;
import com.kpi.labs.timetable.dao.LecturerDAO;
import com.kpi.labs.timetable.dao.LessonDAO;
import com.kpi.labs.timetable.dao.StudentDAO;
import com.kpi.labs.timetable.domain.Graduate;
import com.kpi.labs.timetable.domain.Group;
import com.kpi.labs.timetable.domain.Lesson;
import com.kpi.labs.timetable.domain.Student;
import com.kpi.labs.timetable.domain.User;
import com.kpi.labs.timetable.domain.UserRole;

@Repository
public class SQLStudentDao implements StudentDAO {
    private static final Logger LOG = LoggerFactory.getLogger(SQLStudentDao.class);
    private static final String INSERT_STUDENT_SQL = "INSERT INTO \"USER\" (USER_NAME, ROLE, GROUP_ID) VALUES (?,?,?)";
    private static final String LOAD_GROUP_ID_SQL = "SELECT GROUP_ID FROM \"USER\" WHERE USER_ID=?";
    private static final String LOAD_STUDENTS_FOR_GROUP = "SELECT * FROM \"USER\" WHERE GROUP_ID=?";
    private static final String LOAD_STUDENTS_IDS = "SELECT USER_ID FROM \"USER\"";

    @Autowired
    private JdbcOperations jdbc;
    @Autowired
    private SQLUserDao userDao;
    @Autowired
    private GroupDAO groupDAO;
    @Autowired
    private GraduateDAO graduateDAO;
    @Autowired
    private LessonDAO lessonDAO;
    @Autowired
    private LecturerDAO lecturerDAO;

    @Override
    public Integer create(Student element) {
        jdbc.execute(INSERT_STUDENT_SQL, (PreparedStatementCallback) ps -> {
            ps.setObject(1, element.getName());
            ps.setObject(2, element.getRole().name());
            ps.setObject(3, element.getGroup().getId());
            ps.execute();
            return null;
        });
        LOG.info("Create new Student - {} to Group - {}", element.getName(), element.getGroup().getName());
        return userDao.loadUserIdByName(element.getName());
    }

    @Override
    public void update(Student element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Student element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Student load(Integer key) {
        Student student = new Student();
        User user = userDao.load(key);
        student.setId(user.getId());
        student.setRole(user.getRole());
        student.setName(user.getName());
        student.setPassword(user.getPassword());
        if (UserRole.STUDENT.equals(user.getRole())) {
            Integer groupId = jdbc.queryForObject(LOAD_GROUP_ID_SQL, new Object[]{ key }, Integer.class);
            Group group = groupDAO.load(groupId);
            student.setGroup(group);
            List<Lesson> lessons = group.getLessons();
            student.setLessons(lessons);
            List<Graduate> graduates = new ArrayList<>();
            for (Lesson lesson : lessons) {
                graduates = graduateDAO.loadByStudentIdAndLessonId(key, lesson.getId());
                for (Graduate graduate : graduates) {
                    graduate.setLesson(lesson);
                    graduate.setLecturer(lesson.getLecturer());
                }
            }
            student.setGraduates(graduates);
        }
        return student;
    }

    @Override
    public List<Student> loadStudentsForGroup(Group group) {
        return jdbc.query(LOAD_STUDENTS_FOR_GROUP, new Object[]{ group.getId() }, (RowMapper) (rs, rows) -> {
            Student student = mapStudent(rs);
            student.setGroup(group);
            student.setGraduates(graduateDAO.loadByStudentId(student.getId()));
            return student;
        });
    }

    @Override
    public List<Student> loadAll() {
        List<Student> students = new ArrayList<>();
        List<Integer> ids = jdbc.queryForList(LOAD_STUDENTS_IDS, Integer.class);
        for (Integer id : ids) {
            students.add(load(id));
        }
        return students;
    }

    protected Student mapStudent(ResultSet rs) throws SQLException {
        if (rs.next()) {
            User user = userDao.mapUser(rs);
            Student student = new Student();
            student.setId(user.getId());
            student.setName(user.getName());
            student.setPassword(user.getPassword());
            student.setRole(user.getRole());
            return student;
        }
        return null;
    }
}

