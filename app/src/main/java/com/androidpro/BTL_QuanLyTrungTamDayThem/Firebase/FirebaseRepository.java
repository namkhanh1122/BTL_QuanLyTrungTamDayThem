package com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase;

import androidx.annotation.NonNull;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Attendance;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Student;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {
    private final  FirebaseDatabase database = FirebaseDatabase.getInstance("https://qltrungtamdaythem-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference courseRef;
    private final DatabaseReference lessonRef;
    private final DatabaseReference studentRef;
    private final DatabaseReference documentRef;
    private final DatabaseReference gradeRef;
    private final DatabaseReference attendanceRef;
    private static final FirebaseRepository instance = new FirebaseRepository();

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }

    public FirebaseRepository() {
        courseRef = database.getReference("Courses");
        lessonRef = database.getReference("Lessons");
        studentRef = database.getReference("Students");
        documentRef = database.getReference("Documents");
        gradeRef = database.getReference("Grades");
        attendanceRef = database.getReference("Attendances");
    }

    public static FirebaseRepository getInstance() {
        return instance;
    }

    public void addCourses(Course course, DataCallback<Course> callback) {
        String id = courseRef.push().getKey();

        if (id == null) {
            callback.onError("Failed to create lesson ID");
            return;
        }

        course.setId(id);

        courseRef.child(String.valueOf(course.getId())).setValue(course)
                .addOnSuccessListener(aVoid -> callback.onSuccess(course))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void deleteCourse(String courseId, DataCallback<Course> callback) {
        courseRef.child(courseId).removeValue()
                .addOnSuccessListener(aVoid -> callback.onSuccess(null));
    }

    public void updateCourse(Course course, DataCallback<Course> callback) {
        courseRef.child(course.getId()).setValue(course)
                .addOnSuccessListener(aVoid -> callback.onSuccess(course));
    }

    public void getCourseById(String courseId,DataCallback<Course> callback) {
        courseRef.child(courseId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    Course course = dataSnapshot.getValue(Course.class);
                    callback.onSuccess(course);
                } else {
                    callback.onError("Course not found: " + courseId);
                }
            } else {
                callback.onError(task.getException().getMessage());
            }
        });
    }
    public void listenCourses(DataCallback<List<Course>> callback) {
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Course> list = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Course c = s.getValue(Course.class);
                    if (c != null && c.getInstructorId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        list.add(c);
                    }
                }
                callback.onSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void addStudentToCourse(String courseId, Student student, DataCallback<Student> callback) {
        String id = studentRef.push().getKey();

        if (id == null) {
            callback.onError("Failed to create lesson ID");
            return;
        }
        student.setId(id);

        student.setCourseId(courseId);

        studentRef.child(String.valueOf(student.getId())).setValue(student)
                .addOnCompleteListener(aVoid -> callback.onSuccess(student))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void getStudentById(String studentId,DataCallback<Student> callback) {
        studentRef.child(studentId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    Student student = dataSnapshot.getValue(Student.class);
                    callback.onSuccess(student);
                } else {
                    callback.onError("Course not found: " + studentId);
                }
            } else {
                callback.onError(task.getException().getMessage());
            }
        });
    }

    public void updateStudent(Student student, DataCallback<Student> callback) {
        if (student == null || student.getId() == null) {
            callback.onError("Invalid student");
            return;
        }

        studentRef.child(student.getId()).setValue(student)
                .addOnSuccessListener(aVoid -> callback.onSuccess(student))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void deleteStudent(String studentId, DataCallback<Student> callback) {
        // First remove any attendance records that belong to this student, then remove the student
        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> attendanceIds = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Attendance at = s.getValue(Attendance.class);
                    if (at != null && studentId.equals(at.getStudentId())) {
                        // use the node key (s.getKey()) which should equal at.getId() when created
                        String key = s.getKey();
                        if (key != null) attendanceIds.add(key);
                    }
                }

                if (attendanceIds.isEmpty()) {
                    // No attendances to remove, delete student directly
                    studentRef.child(studentId).removeValue()
                            .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                            .addOnFailureListener(e -> callback.onError(e.getMessage()));
                    return;
                }

                final java.util.concurrent.atomic.AtomicInteger remaining = new java.util.concurrent.atomic.AtomicInteger(attendanceIds.size());
                final java.util.concurrent.atomic.AtomicInteger failed = new java.util.concurrent.atomic.AtomicInteger(0);

                for (String aid : attendanceIds) {
                    attendanceRef.child(aid).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                if (remaining.decrementAndGet() == 0) {
                                    if (failed.get() == 0) {
                                        // all attendance removed -> remove student
                                        studentRef.child(studentId).removeValue()
                                                .addOnSuccessListener(aVoid2 -> callback.onSuccess(null))
                                                .addOnFailureListener(e -> callback.onError(e.getMessage()));
                                    } else {
                                        callback.onError("Failed to remove some attendance records");
                                    }
                                }
                            })
                            .addOnFailureListener(e -> {
                                failed.incrementAndGet();
                                if (remaining.decrementAndGet() == 0) {
                                    callback.onError("Failed to remove some attendance records: " + e.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void listenStudentsInCourses(String courseId, DataCallback<List<Student>> callback) {
        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Student> students = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Student student = s.getValue(Student.class);
                    if (student != null && student.getCourseId().equals(courseId))
                    {
                        students.add(student);
                    }
                }
                callback.onSuccess(students);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void addLessonToCourse(String courseId, Lesson lesson, DataCallback<Lesson> callback) {
        String lessonId = lessonRef.push().getKey();

        if (lessonId == null) {
            callback.onError("Failed to create lesson ID");
            return;
        }
        lesson.setId(lessonId);

        lesson.setCourseId(courseId);


        lessonRef.child(lessonId).setValue(lesson)
                .addOnCompleteListener(aVoid -> callback.onSuccess(lesson))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void updateLesson(Lesson lesson, DataCallback<Lesson> callback) {
        if (lesson == null || lesson.getId() == null) {
            callback.onError("Invalid lesson");
            return;
        }

        lessonRef.child(lesson.getId()).setValue(lesson)
                .addOnSuccessListener(aVoid -> callback.onSuccess(lesson))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void deleteLesson(String lessonId, DataCallback<Lesson> callback) {
        lessonRef.child(lessonId).removeValue()
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }


    public void getLessonById(String lessonId, DataCallback<Lesson> callback) {
        lessonRef.child(lessonId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    Lesson lesson = dataSnapshot.getValue(Lesson.class);
                    callback.onSuccess(lesson);
                } else {
                    callback.onError("Course not found: " + lessonId);
                }
            } else {
                callback.onError(task.getException().getMessage());
            }
        });
    }

    public void listenLessonsRealtime(DataCallback<List<Lesson>> callback) {
        lessonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Lesson> lessons = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Lesson lesson = s.getValue(Lesson.class);
                    if (lesson != null)
                    {
                        lessons.add(lesson);
                    }
                }
                callback.onSuccess(lessons);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void listenLessonsInCourse(String courseId, DataCallback<List<Lesson>> callback) {
        lessonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Lesson> lessons = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Lesson lesson = s.getValue(Lesson.class);
                    if (lesson != null && lesson.getCourseId().equals(courseId))
                    {
                        lessons.add(lesson);
                    }
                }
                callback.onSuccess(lessons);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void addAttendance(Attendance attendance, DataCallback<Attendance> callback) {
        String id = attendanceRef.push().getKey();
        if (id == null) {
            callback.onError("Failed to create attendance ID");
            return;
        }
        attendance.setId(id);

        attendanceRef.child(id).setValue(attendance)
                .addOnSuccessListener(aVoid -> callback.onSuccess(attendance))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void listenAttendanceInLesson(String lessonId, DataCallback<List<Attendance>> callback) {
        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Attendance> attendances = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Attendance attendance = s.getValue(Attendance.class);
                    if (attendance != null && attendance.getLessonId().equals(lessonId))
                    {
                        attendances.add(attendance);
                    }
                }
                callback.onSuccess(attendances);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void getAttendancesByStudentId(String studentId, DataCallback<List<Attendance>> callback) {
        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Attendance> attendances = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Attendance attendance = s.getValue(Attendance.class);
                    if (attendance != null && studentId != null && studentId.equals(attendance.getStudentId())) {
                        attendances.add(attendance);
                    }
                }
                callback.onSuccess(attendances);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void updateAttendance(Attendance attendance, DataCallback<Attendance> callback) {
        if (attendance == null || attendance.getId() == null) {
            callback.onError("Invalid attendance data");
            return;
        }

        attendanceRef.child(attendance.getId()).setValue(attendance)
                .addOnSuccessListener(aVoid -> callback.onSuccess(attendance))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
}
