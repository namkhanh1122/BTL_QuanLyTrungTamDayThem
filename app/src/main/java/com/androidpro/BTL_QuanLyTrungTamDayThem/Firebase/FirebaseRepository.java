package com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Attendance;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Course;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Document;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Student;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;
import java.util.List;

public class FirebaseRepository {

    private final FirebaseFirestore db;
    private final CollectionReference coursesRef;
    private final CollectionReference studentsRef;

    private static final FirebaseRepository instance = new FirebaseRepository();

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String error);
    }

    private FirebaseRepository() {
        db = FirebaseFirestore.getInstance();
        coursesRef = db.collection("Courses");
        studentsRef = db.collection("Students");
    }

    public static FirebaseRepository getInstance() {
        return instance;
    }

    public void addCourse(Course course, DataCallback<Course> callback) {
        DocumentReference docRef = coursesRef.document();
        course.setId(docRef.getId());
        docRef.set(course)
                .addOnSuccessListener(aVoid -> callback.onSuccess(course))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void getCourse(String courseId, DataCallback<Course> callback) {
        coursesRef.document(courseId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        callback.onSuccess(doc.toObject(Course.class));
                    } else {
                        callback.onError("Không tìm thấy khóa học.");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void updateCourse(Course course, DataCallback<Course> callback) {
        coursesRef.document(course.getId()).set(course, SetOptions.merge())
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void deleteCourse(String courseId, DataCallback<Course> callback) {
        coursesRef.document(courseId).delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void listenCoursesForInstructor(String instructorId, DataCallback<List<Course>> callback) {
        coursesRef.whereEqualTo("instructorId", instructorId)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        callback.onError(error.getMessage());
                        return;
                    }
                    if (snapshot != null) {
                        callback.onSuccess(snapshot.toObjects(Course.class));
                    }
                });
    }

    public void addStudent(Student student, DataCallback<Student> callback) {
        DocumentReference docRef = studentsRef.document();
        student.setId(docRef.getId());
        docRef.set(student)
                .addOnSuccessListener(aVoid -> callback.onSuccess(student))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void updateStudent(Student student, DataCallback<Student> callback) {
        if (student == null || student.getId() == null) {
            callback.onError("Invalid student data");
            return;
        }
        studentsRef.document(student.getId()).set(student, SetOptions.merge())
                .addOnSuccessListener(aVoid -> callback.onSuccess(student))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void getStudent(String studentId, DataCallback<Student> callback) {
        studentsRef.document(studentId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        callback.onSuccess(doc.toObject(Student.class));
                    } else {
                        callback.onError("Không tìm thấy sinh viên.");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void deleteStudent(String studentId, DataCallback<Student> callback) {
        db.collectionGroup("Attendances").whereEqualTo("studentId", studentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        callback.onError("Lỗi khi tìm điểm danh: " + task.getException().getMessage());
                        return;
                    }

                    WriteBatch batch = db.batch();

                    if (!task.getResult().isEmpty()) {
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            batch.delete(doc.getReference());
                        }
                    }

                    batch.delete(studentsRef.document(studentId));

                    batch.commit()
                            .addOnSuccessListener(aVoid -> callback.onSuccess(null)) // Trả về null
                            .addOnFailureListener(e -> callback.onError(e.getMessage()));
                });
    }

    public void listenStudentsInCourse(String courseId, DataCallback<List<Student>> callback) {
        studentsRef.whereEqualTo("courseId", courseId)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        callback.onError(error.getMessage());
                        return;
                    }
                    if (snapshot != null) {
                        callback.onSuccess(snapshot.toObjects(Student.class));
                    }
                });
    }

    private CollectionReference getLessonsCol(String courseId) {
        return coursesRef.document(courseId).collection("Lessons");
    }

    private void findLessonRefById(String lessonId, DataCallback<DocumentReference> callback) {
        if (lessonId == null || lessonId.isEmpty()) {
            callback.onError("Lesson ID không hợp lệ.");
            return;
        }

        db.collectionGroup("Lessons").whereEqualTo("id", lessonId)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot == null || querySnapshot.isEmpty()) {
                        callback.onError("Không tìm thấy lesson với ID: " + lessonId);
                        return;
                    }

                    DocumentReference lessonRef = querySnapshot.getDocuments().get(0).getReference();
                    callback.onSuccess(lessonRef);
                })
                .addOnFailureListener(e -> callback.onError("Lỗi khi tìm lesson: " + e.getMessage()));
    }

    public void addLesson(String courseId, Lesson lesson, DataCallback<Lesson> callback) {
        DocumentReference docRef = getLessonsCol(courseId).document();
        lesson.setId(docRef.getId());
        lesson.setCourseId(courseId);
        docRef.set(lesson)
                .addOnSuccessListener(aVoid -> callback.onSuccess(lesson))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void updateLesson(Lesson lesson, DataCallback<Lesson> callback) {
        if (lesson == null || lesson.getId() == null) {
            callback.onError("Invalid lesson data");
            return;
        }
        findLessonRefById(lesson.getId(), new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference lessonRef) {
                lessonRef.set(lesson, SetOptions.merge())
                        .addOnSuccessListener(aVoid -> callback.onSuccess(lesson))
                        .addOnFailureListener(e -> callback.onError(e.getMessage()));
            }

            @Override
            public void onError(String error) {
                callback.onError("Lỗi khi cập nhật lesson: " + error);
            }
        });
    }

    public void listenLessonsInCourse(String courseId, DataCallback<List<Lesson>> callback) {
        getLessonsCol(courseId).orderBy("beginTime")
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        callback.onError(error.getMessage());
                        return;
                    }
                    if (snapshot != null) {
                        callback.onSuccess(snapshot.toObjects(Lesson.class));
                    }
                });
    }

    public void deleteLesson(String lessonId, DataCallback<Lesson> callback) {
        findLessonRefById(lessonId, new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference lessonRef) {
                lessonRef.delete()
                        .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                        .addOnFailureListener(e -> callback.onError(e.getMessage()));
            }
            @Override
            public void onError(String error) {
                callback.onError("Lỗi khi xóa lesson: " + error);
            }
        });
    }

    public void getLesson(String lessonId, DataCallback<Lesson> callback) {
        findLessonRefById(lessonId, new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference lessonRef) {
                lessonRef.get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Lesson lesson = documentSnapshot.toObject(Lesson.class);
                            callback.onSuccess(lesson);
                        } else {
                            callback.onError("Không tìm thấy tài liệu (có thể đã bị xóa).");
                        }
                    })
                    .addOnFailureListener(e -> {
                        callback.onError("Lỗi khi lấy dữ liệu lesson: " + e.getMessage());
                    });
            }
            @Override
            public void onError(String error) {
                callback.onError("Lỗi khi tìm lesson: " + error);
            }
        });
    }

    private void findAttendanceRefById(String attendanceId, DataCallback<DocumentReference> callback) {
        if (attendanceId == null || attendanceId.isEmpty()) {
            callback.onError("Attendance ID không hợp lệ.");
            return;
        }

        db.collectionGroup("Attendances").whereEqualTo("id", attendanceId)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot == null || querySnapshot.isEmpty()) {
                        callback.onError("Không tìm thấy dữ liệu với ID: " + attendanceId);
                        return;
                    }
                    DocumentReference attRef = querySnapshot.getDocuments().get(0).getReference();
                    callback.onSuccess(attRef);
                })
                .addOnFailureListener(e -> callback.onError("Lỗi khi tìm attendance: " + e.getMessage()));
    }

    public void addAttendance(String lessonId, Attendance attendance, DataCallback<Attendance> callback) {
        if (attendance == null) {
            callback.onError("Dữ liệu Attendance bị null");
            return;
        }

        findLessonRefById(lessonId, new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference lessonRef) {

                CollectionReference attendanceCol = lessonRef.collection("Attendances");

                DocumentReference docRef = attendanceCol.document();
                attendance.setId(docRef.getId());
                attendance.setLessonId(lessonId);

                docRef.set(attendance)
                        .addOnSuccessListener(aVoid -> callback.onSuccess(attendance))
                        .addOnFailureListener(e -> callback.onError(e.getMessage()));
            }

            @Override
            public void onError(String error) {
                // Lỗi từ findLessonRefById (không tìm thấy lesson)
                callback.onError(error);
            }
        });
    }

    public void updateAttendance(Attendance attendance, DataCallback<Attendance> callback) {
        if (attendance == null || attendance.getId() == null) {
            callback.onError("Invalid attendance data");
            return;
        }
        findAttendanceRefById(attendance.getId(), new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference attRef) {
                attRef.set(attendance, SetOptions.merge())
                        .addOnSuccessListener(aVoid -> callback.onSuccess(attendance))
                        .addOnFailureListener(e -> callback.onError(e.getMessage()));
            }
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void deleteAttendance(String attendanceId, DataCallback<Void> callback) {
        findAttendanceRefById(attendanceId, new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference attRef) {
                attRef.delete()
                        .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                        .addOnFailureListener(e -> callback.onError(e.getMessage()));
            }
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getAttendance(String attendanceId, DataCallback<Attendance> callback) {
        findAttendanceRefById(attendanceId, new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference attRef) {
                attRef.get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                callback.onSuccess(documentSnapshot.toObject(Attendance.class));
                            } else {
                                callback.onError("Không tìm thấy tài liệu.");
                            }
                        })
                        .addOnFailureListener(e -> {
                            callback.onError("Lỗi khi lấy dữ liệu attendance: " + e.getMessage());
                        });
            }
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getAttendancesInLesson(String lessonId, DataCallback<List<Attendance>> callback) {
        findLessonRefById(lessonId, new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference lessonRef) {
                lessonRef.collection("Attendances").get()
                        .addOnSuccessListener(snapshot -> {
                            callback.onSuccess(snapshot.toObjects(Attendance.class));
                        })
                        .addOnFailureListener(e -> callback.onError(e.getMessage()));
            }
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void getAttendancesForStudent(String studentId, DataCallback<List<Attendance>> callback) {
        db.collectionGroup("Attendances").whereEqualTo("studentId", studentId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    callback.onSuccess(snapshot.toObjects(Attendance.class));
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void listenAttendancesInLesson(String lessonId, DataCallback<List<Attendance>> callback) {
        findLessonRefById(lessonId, new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference lessonRef) {
                lessonRef.collection("Attendances")
                        .addSnapshotListener((snapshot, error) -> {
                            if (error != null) {
                                callback.onError(error.getMessage());
                                return;
                            }
                            if (snapshot != null) {
                                callback.onSuccess(snapshot.toObjects(Attendance.class));
                            }
                        });
            }
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    private void findDocumentRefById(String documentId, DataCallback<DocumentReference> callback) {
        if (documentId == null || documentId.isEmpty()) {
            callback.onError("Document ID không hợp lệ.");
            return;
        }
        db.collectionGroup("Documents").whereEqualTo("id", documentId)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot == null || querySnapshot.isEmpty()) {
                        callback.onError("Không tìm thấy document với ID: " + documentId);
                        return;
                    }
                    callback.onSuccess(querySnapshot.getDocuments().get(0).getReference());
                })
                .addOnFailureListener(e -> callback.onError("Lỗi khi tìm document: " + e.getMessage()));
    }

    public void addDocument(String lessonId, Document document, DataCallback<Document> callback) {
        if (document == null) {
            callback.onError("Dữ liệu Document bị null");
            return;
        }
        // 1. Tìm Lesson cha
        findLessonRefById(lessonId, new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference lessonRef) {
                // 2. Thêm Document vào sub-collection
                CollectionReference docCol = lessonRef.collection("Documents");
                DocumentReference docRef = docCol.document();
                document.setId(docRef.getId());
                // Giả sử model Document có setLessonId(lessonId)
                // document.setLessonId(lessonId);

                docRef.set(document)
                        .addOnSuccessListener(aVoid -> callback.onSuccess(document))
                        .addOnFailureListener(e -> callback.onError(e.getMessage()));
            }
            @Override
            public void onError(String error) {
                callback.onError(error); // Lỗi không tìm thấy Lesson
            }
        });
    }

    public void getDocument(String documentId, DataCallback<Document> callback) {
        findDocumentRefById(documentId, new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference docRef) {
                docRef.get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                callback.onSuccess(documentSnapshot.toObject(Document.class));
                            } else {
                                callback.onError("Không tìm thấy tài liệu.");
                            }
                        })
                        .addOnFailureListener(e -> {
                            callback.onError("Lỗi khi lấy dữ liệu document: " + e.getMessage());
                        });
            }
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void updateDocument(Document document, DataCallback<Document> callback) {
        if (document == null || document.getId() == null) {
            callback.onError("Invalid document data");
            return;
        }
        findDocumentRefById(document.getId(), new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference docRef) {
                docRef.set(document, SetOptions.merge())
                        .addOnSuccessListener(aVoid -> callback.onSuccess(document))
                        .addOnFailureListener(e -> callback.onError(e.getMessage()));
            }
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void deleteDocument(String documentId, DataCallback<Void> callback) {
        findDocumentRefById(documentId, new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference docRef) {
                docRef.delete()
                        .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                        .addOnFailureListener(e -> callback.onError(e.getMessage()));
            }
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public void listenDocumentsInLesson(String lessonId, DataCallback<List<Document>> callback) {
        findLessonRefById(lessonId, new DataCallback<>() {
            @Override
            public void onSuccess(DocumentReference lessonRef) {
                lessonRef.collection("Documents").addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        callback.onError(error.getMessage());
                        return;
                    }
                    if (snapshot != null) {
                        callback.onSuccess(snapshot.toObjects(Document.class));
                    }
                });
            }
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public Task<QuerySnapshot> getCoursesForInstructor_Task(String instructorId) {
        return coursesRef.whereEqualTo("instructorId", instructorId).get();
    }

    public Task<QuerySnapshot> getFutureLessonsInCourse_Task(String courseId) {
        return getLessonsCol(courseId)
                .whereGreaterThan("beginTime", new Date())
                .get();
    }
}