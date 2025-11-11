package com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Activities;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Firebase.FirebaseRepository;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Document;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Firebase.Lesson;
import com.androidpro.BTL_QuanLyTrungTamDayThem.R;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Views.Adapters.DocumentsAdapter;
import com.androidpro.BTL_QuanLyTrungTamDayThem.databinding.ActivityDocumentBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import com.androidpro.BTL_QuanLyTrungTamDayThem.Core.BaseActivity;
import com.androidpro.BTL_QuanLyTrungTamDayThem.ViewModels.DocumentViewModel;
import com.androidpro.BTL_QuanLyTrungTamDayThem.Models.Enums.ScheduleStatus; // Thêm import này

public class DocumentActivity extends BaseActivity {
    private ActivityDocumentBinding binding;
    private DocumentsAdapter adapter;
    private String lessonId;
    private String lessonTitle;

    // Mặc định là false (fail-closed) cho đến khi quyền được xác nhận
    private boolean actionsAllowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initUI() {
        binding = ActivityDocumentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lessonId = getIntent().getStringExtra("lesson_id");
        lessonTitle = getIntent().getStringExtra("lesson_title");
        if (lessonTitle == null) lessonTitle = "Tài liệu";

        if (lessonId == null || lessonId.isEmpty()) {
            if (viewModel != null) viewModel.sendNotification("Lỗi: Không tìm thấy ID buổi học");
            finish();
            return;
        }

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(lessonTitle);
        }

        adapter = new DocumentsAdapter();
        binding.rvDocuments.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDocuments.setAdapter(adapter);

        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.tvAddDocumentLink.setOnClickListener(v -> showAddDialog(null));

        // Bắt đầu ở trạng thái bị vô hiệu hóa, sẽ được kích hoạt trong loadEvents nếu được phép
        binding.tvAddDocumentLink.setVisibility(View.GONE);
    }

    @Override
    public void initViewModel() {
        viewModel = new ViewModelProvider(this).get(DocumentViewModel.class);
    }

    @Override
    public void loadEvents() {
        if (lessonId == null || lessonId.isEmpty()) {
            viewModel.sendNotification("Lỗi: Không tìm thấy ID buổi học");
            finish();
            return;
        }

        ((DocumentViewModel) viewModel).listenDocumentsInLesson(lessonId);

        FirebaseRepository.getInstance().getLesson(lessonId, new FirebaseRepository.DataCallback<>() {
            @Override
            public void onSuccess(Lesson lesson) {
                if (lesson == null) {
                    // Lỗi: Không tìm thấy buổi học -> không cho phép hành động (An toàn)
                    actionsAllowed = false;
                    binding.tvAddDocumentLink.setVisibility(View.GONE);
                    viewModel.sendNotification("Lỗi: Không tìm thấy thông tin buổi học.");
                    return;
                }

                // KIỂM TRA TRẠNG THÁI LESSON
                if (lesson.getStatus() != null) {
                    ScheduleStatus st = lesson.getStatus();
                    actionsAllowed = st == ScheduleStatus.Active;
                } else {
                    // Fallback: nếu status là null, coi như là Sắp/Đang diễn ra -> cho phép
                    actionsAllowed = true;
                }

                // Áp dụng quyền cho UI
                if (!actionsAllowed) {
                    binding.tvAddDocumentLink.setVisibility(View.GONE);
                } else {
                    binding.tvAddDocumentLink.setVisibility(View.VISIBLE);
                    binding.tvAddDocumentLink.setEnabled(true);
                    // Chỉ gắn hành động trượt (swipe) NẾU được phép
                    setupSwipeActions();
                }
            }

            @Override
            public void onError(String error) {
                // Lỗi khi lấy buổi học -> không cho phép hành động (An toàn)
                actionsAllowed = false;
                binding.tvAddDocumentLink.setVisibility(View.GONE);
                viewModel.sendNotification("Lỗi khi lấy thông tin buổi học: " + error);
            }
        });
    }

    @Override
    public void observeData() {
        super.observeData();
        ((DocumentViewModel) viewModel).documents.observe(this, data -> {
            adapter.submitList(data);
        });
    }

    private void setupSwipeActions() {
        // Chỉ được gọi nếu actionsAllowed là true
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // ... (code onSwiped của bạn vẫn giữ nguyên)
                int position = viewHolder.getBindingAdapterPosition();
                final Document document = adapter.getDocumentAt(position);

                if (direction == ItemTouchHelper.LEFT) {
                    new AlertDialog.Builder(DocumentActivity.this)
                            .setTitle("Xóa tài liệu")
                            .setMessage("Bạn có muốn xóa tài liệu \"" + document.getTitle() + "\" không?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                ((DocumentViewModel) viewModel).deleteDocument(document);

                                Snackbar.make(binding.rvDocuments, "Đã xóa " + document.getTitle(), Snackbar.LENGTH_LONG)
                                        .setAction("Hoàn tác", v -> {
                                            ((DocumentViewModel) viewModel).addDocument(lessonId, document);
                                        })
                                        .show();
                            })
                            .setNegativeButton("Hủy", (dialog, which) -> {
                                adapter.notifyItemChanged(position);
                            })
                            .setOnCancelListener(dialog -> adapter.notifyItemChanged(position))
                            .show();
                } else if (direction == ItemTouchHelper.RIGHT) {
                    showAddDialog(document); // Mở dialog ở chế độ sửa
                    adapter.notifyItemChanged(position); // Trả lại item về vị trí cũ
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // ... (code onChildDraw của bạn vẫn giữ nguyên)
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    Drawable icon;
                    ColorDrawable background = new ColorDrawable();
                    int iconMarginVertical = (itemView.getHeight() - 72) / 2;
                    int iconMarginHorizontal = 40;

                    if (dX > 0) {
                        icon = ContextCompat.getDrawable(DocumentActivity.this, R.drawable.ic_edit_24);
                        background.setColor(Color.parseColor("#3498db"));
                        background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
                        background.draw(c);
                        int iconTop = itemView.getTop() + iconMarginVertical;
                        int iconBottom = iconTop + icon.getIntrinsicHeight();
                        int iconLeft = itemView.getLeft() + iconMarginHorizontal;
                        int iconRight = itemView.getLeft() + iconMarginHorizontal + icon.getIntrinsicWidth();
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);
                    } else if (dX < 0) {
                        icon = ContextCompat.getDrawable(DocumentActivity.this, R.drawable.ic_delete_24);
                        background.setColor(Color.parseColor("#e74c3c"));
                        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        background.draw(c);
                        int iconTop = itemView.getTop() + iconMarginVertical;
                        int iconBottom = iconTop + icon.getIntrinsicHeight();
                        int iconRight = itemView.getRight() - iconMarginHorizontal;
                        int iconLeft = itemView.getRight() - iconMarginHorizontal - icon.getIntrinsicWidth();
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);
                    } else {
                        background.setBounds(0, 0, 0, 0);
                        background.draw(c);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // Đã kiểm tra actionsAllowed bên ngoài trước khi gọi setupSwipeActions
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.rvDocuments);
    }

    private void showAddDialog(Document document) {
        // ... (code showAddDialog của bạn vẫn giữ nguyên)
        boolean isEditMode = (document != null);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_document, null, false);
        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etDesc = view.findViewById(R.id.etDescription);
        EditText etUrl = view.findViewById(R.id.etUrl);

        String dialogTitle = isEditMode ? "Sửa tài liệu" : "Thêm tài liệu";
        String positiveButtonText = isEditMode ? "Lưu" : "Thêm";

        if (isEditMode) {
            etTitle.setText(document.getTitle());
            etDesc.setText(document.getDescription());
            etUrl.setText(document.getUrl());
        }

        new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setView(view)
                .setPositiveButton(positiveButtonText, (dialog, which) -> {
                    String title = etTitle.getText().toString().trim();
                    String desc = etDesc.getText().toString().trim();
                    String url = etUrl.getText().toString().trim();

                    if (TextUtils.isEmpty(title) || TextUtils.isEmpty(url)) {
                        Snackbar.make(binding.getRoot(), "Cần nhập tiêu đề và URL", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                    if (isEditMode) {
                        document.setTitle(title);
                        document.setDescription(desc);
                        document.setUrl(url);
                        ((DocumentViewModel) viewModel).updateDocument(document);
                    } else {
                        Document doc = new Document();
                        doc.setTitle(title);
                        doc.setDescription(desc);
                        doc.setUrl(url);
                        doc.setLessonId(lessonId);
                        ((DocumentViewModel) viewModel).addDocument(lessonId, doc);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}