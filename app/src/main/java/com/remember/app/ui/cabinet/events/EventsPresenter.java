package com.remember.app.ui.cabinet.events;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

@InjectViewState
public class EventsPresenter extends BasePresenter<EventView> {

    public EventsPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    public void getEvents() {
        List<MemoryPageModel> memoryPageModelList = new ArrayList<>();
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Субботнее воскресенье", 246, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Рождественское рождество", 146, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Субботнее воскресенье", 23, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Рождественское рождество", 321, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Субботнее воскресенье", 216, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Рождественское рождество", 246, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Субботнее воскресенье", 146, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Рождественское рождество", 23, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Субботнее воскресенье", 321, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        memoryPageModelList.add(new MemoryPageModel("02.04.1960", "02.04.2019",
                "Рождественское рождество", 216, "https://i1.sndcdn.com/artworks-000058012723-2rmkph-t500x500.jpg"));
        getViewState().getEvents(memoryPageModelList);
    }

}
