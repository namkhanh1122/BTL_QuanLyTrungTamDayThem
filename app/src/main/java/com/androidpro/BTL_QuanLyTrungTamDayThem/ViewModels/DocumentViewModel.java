package com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Document;

import java.util.List;

public class DocumentViewModel extends BaseViewModel {
    public final MutableLiveData<List<Document>> documents = new MutableLiveData<>();

    public DocumentViewModel(@NonNull Application application) {
        super(application);
    }

    public void listenDocumentsInLesson(String lessonId) {
        FirebaseRepository.getInstance().listenDocumentsInLesson(lessonId, new FirebaseRepository.DataCallback<List<Document>>() {
            @Override
            public void onSuccess(List<Document> data) {
                documents.postValue(data);
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue(error);
            }
        });
    }

    public void addDocument(String lessonId, Document document) {
        FirebaseRepository.getInstance().addDocument(lessonId, document, new FirebaseRepository.DataCallback<Document>() {
            @Override
            public void onSuccess(Document data) {
                notifyMessage.postValue("Đã thêm tài liệu");
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue("Lỗi khi thêm: " + error);
            }
        });
    }

    public void updateDocument(Document document) {
        FirebaseRepository.getInstance().updateDocument(document, new FirebaseRepository.DataCallback<Document>() {
            @Override
            public void onSuccess(Document data) {
                notifyMessage.postValue("Cập nhật tài liệu thành công");
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue("Lỗi khi cập nhật: " + error);
            }
        });
    }

    public void deleteDocument(Document document) {
        FirebaseRepository.getInstance().deleteDocument(document.getId(), new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Document data) {
            }

            @Override
            public void onError(String error) {
                notifyMessage.postValue("Lỗi khi xóa: " + error);
            }
        });
    }
}