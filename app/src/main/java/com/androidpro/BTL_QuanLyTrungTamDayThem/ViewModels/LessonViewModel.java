package com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Attendance;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Student;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        // 1. Lấy thông tin buổi học để tìm Course ID
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
                    public void onSuccess(List<Student> allStudents) {
                        if (allStudents == null || allStudents.isEmpty()) {
                            attendanceList.postValue(new java.util.ArrayList<>());
                            return;
                        }

                        FirebaseRepository.getInstance().listenAttendancesInLesson(lessonId, new FirebaseRepository.DataCallback<>() {
                            @Override
                            public void onSuccess(List<Attendance> existingAttendances) {

                                Set<String> attendedStudentIds = new HashSet<>();
                                if (existingAttendances != null) {
                                    for (Attendance a : existingAttendances) {
                                        attendedStudentIds.add(a.getStudentId());
                                    }
                                }

                                List<Student> missingStudents = new ArrayList<>();
                                for (Student s : allStudents) {
                                    if (!attendedStudentIds.contains(s.getId())) {
                                        missingStudents.add(s);
                                    }
                                }

                                if (missingStudents.isEmpty()) {
                                    attendanceList.postValue(existingAttendances);
                                } else {
                                    for (Student s : missingStudents) {
                                        Attendance a = new Attendance();
                                        a.setLessonId(lessonId);
                                        a.setStudentId(s.getId());
                                        a.setPresent(false);
                                        a.setTimestamp(new java.util.Date());
                                        a.setScore(0);
                                        a.setName(s.getName());


                                        FirebaseRepository.getInstance().addAttendance(lessonId, a, new FirebaseRepository.DataCallback<>() {
                                            @Override
                                            public void onSuccess(Attendance data) {
                                            }

                                            @Override
                                            public void onError(String error) {
                                                notifyMessage.postValue("Lỗi tạo điểm danh: " + error);
                                            }
                                        });
                                    }

                                    if (existingAttendances != null) {
                                        attendanceList.postValue(existingAttendances);
                                    }
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
