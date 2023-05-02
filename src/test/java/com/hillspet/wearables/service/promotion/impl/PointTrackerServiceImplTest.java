package com.hillspet.wearables.service.promotion.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hillspet.wearables.dao.pointTracker.PointTrackerDao;
import com.hillspet.wearables.dto.PointTracker;

@ExtendWith(SpringExtension.class)
class PointTrackerServiceImplTest {

    @InjectMocks
    PointTrackerServiceImpl pointTrackerService;

    @Mock
    PointTrackerDao pointTrackerDao;


    @Test
    @DisplayName("Test get Point Tracking List Service")
    void getPointTrackingList() {

    }

    @Test
    @DisplayName("Test get Point Tracking by Id")
    void getPointTrackerByIdSuccess() {
        PointTracker pointTracker = new PointTracker();
        pointTracker.setStudyName("AGL Technology Inc");
        pointTracker.setStudyId(1);
        pointTracker.setStartDate(LocalDate.parse("2020-10-12"));
        pointTracker.setEndDate(LocalDate.parse("2021-10-12"));
        pointTracker.setStatus(1);
        pointTracker.setTotalPoints("10");
        pointTracker.setActivities("Observations");
        pointTracker.setActivityIds("1");
        Mockito.when(pointTrackerDao.getPointTrackerById(1)).thenReturn(pointTracker);
        PointTracker response = pointTrackerService.getPointTrackerById(1);
        assertEquals(1, response.getStudyId());
    }
}