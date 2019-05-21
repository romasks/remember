package com.remember.app.ui.cabinet.memory_pages;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class PagePresenter extends BasePresenter<PageView> {

    public PagePresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void getPages() {
        List<MemoryPageModel> memoryPageModelList = new ArrayList<>();
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Иванов Иван Иванович", 246, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Степанов Михаил Георгиевич", 146, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Жириновский Евгений Петрович", 23, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Зубенко Иван Иванович", 321, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Носик Сергей Валерьевич", 216, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Иванов Иван Иванович", 246, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Степанов Михаил Георгиевич", 146, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Жириновский Евгений Петрович", 23, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Зубенко Иван Иванович", 321, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Носик Сергей Валерьевич", 216, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        getViewState().getPages(memoryPageModelList);
    }
}
