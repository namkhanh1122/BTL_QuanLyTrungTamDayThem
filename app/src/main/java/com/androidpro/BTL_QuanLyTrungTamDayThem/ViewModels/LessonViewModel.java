package com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Attendance;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Student;

import java.util.List;

public class LessonViewModel extends BaseViewModel {
    public MutableLiveData<Lesson> lessonDetail = new MutableLiveData<>();
    public MutableLiveData<List<Lesson>> lessonList = new MutableLiveData<>();
    public MutableLiveData<List<Student>> studentList = new MutableLiveData<>();
    public MutableLiveData<List<Attendance>> attendanceList = new MutableLiveData<>();

    private String courseId;

    public LessonViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadLessonDetails(String lessonId) {
        FirebaseRepository.getInstance().getLesson(lessonId, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Lesson data) {
                lessonDetail.postValue(data);
            }
            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }

    public void loadLessonsInCourseRealtime(String courseId) {
        this.courseId = courseId;
        FirebaseRepository.getInstance().listenLessonsInCourse(courseId, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(List<Lesson> data) {
                lessonList.postValue(data);
                //repository.insertAll(data);
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }


    public void addLesson(Lesson lesson) {
        FirebaseRepository.getInstance().addLesson(courseId, lesson, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Lesson data) {
                notifyMessage.postValue("Thêm buổi học thành công");
            }
            @Override
            public void onError(String error) {
                notifyMessage.postValue("Lỗi: " + error);
            }
        });
    }

    public void updateLesson(Lesson lesson) {
        FirebaseRepository.getInstance().updateLesson(lesson, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Lesson data) {
                notifyMessage.postValue("Cập nhật buổi học thành công");
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue("Lỗi: " + error);
            }
        });
    }

    // Load attendance entries for a lesson
    public void loadAttendanceForLesson(String lessonId) {
        FirebaseRepository.getInstance().listenAttendancesInLesson(lessonId, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(List<Attendance> data) {
                if (data != null && !data.isEmpty()) {
                    attendanceList.postValue(data);
                } else {
                    // No attendance records yet for this lesson -> create default attendance entries from students in the course
                    FirebaseRepository.getInstance().getLesson(lessonId, new FirebaseRepository.DataCallback<>() {
                        @Override
                        public void onSuccess(Lesson lesson) {
                            if (lesson == null || lesson.getCourseId() == null) {
                                notifyMessage.postValue("Không tìm thấy thông tin khoá học để tạo danh sách điểm danh");
                                return;
                            }

                            String courseId = lesson.getCourseId();
                            FirebaseRepository.getInstance().listenStudentsInCourse(courseId, new FirebaseRepository.DataCallback<>() {
                                @Override
                                public void onSuccess(List<Student> students) {
                                    if (students == null || students.isEmpty()) {
                                        attendanceList.postValue(new java.util.ArrayList<>());
                                        return;
                                    }

                                    java.util.List<Attendance> created = new java.util.ArrayList<>();
                                    for (Student s : students) {
                                        Attendance a = new Attendance();
                                        a.setLessonId(lessonId);
                                        a.setStudentId(s.getId());
                                        a.setPresent(false);
                                        a.setTimestamp(new java.util.Date());
                                        a.setScore(0);
                                        a.setName(s.getName());

                                        // addAttendance will set id on the same Attendance instance
                                        FirebaseRepository.getInstance().addAttendance(lessonId, a, new FirebaseRepository.DataCallback<>() {
                                            @Override
                                            public void onSuccess(Attendance data) {
                                                created.add(data);
                                                if (created.size() == students.size()) {
                                                    attendanceList.postValue(created);
                                                }
                                            }

                                            @Override
                                            public void onError(String error) {
                                                notifyMessage.postValue("Lỗi tạo điểm danh: " + error);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onError(String error) {
                                    notifyMessage.postValue(error);
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            notifyMessage.postValue(error);
                        }
                    });
                }
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }

    // Update an attendance record (present flag and/or score)
    public void updateAttendance(Attendance attendance) {
        FirebaseRepository.getInstance().updateAttendance(attendance, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Attendance data) {
                notifyMessage.postValue("Cập nhật điểm/danh sách điểm danh thành công");
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue("Lỗi: " + error);
            }
        });
    }
}
