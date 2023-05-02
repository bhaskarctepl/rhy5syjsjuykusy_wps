package com.hillspet.wearables.dao.pointTracker.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hillspet.wearables.dto.PointTracker;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PointTrackerDaoImplTest {

    @Autowired
    PointTrackerDaoImpl pointTrackerDao;

   /* @Mock
    JdbcTemplate jdbcTemplate;
*/
    @Test
    void getPointTrackerById() {
        PointTracker pointTracker = new PointTracker();
        pointTracker.setStudyName("AGL Technology Inc");
        pointTracker.setStudyId(1);
        pointTracker.setStartDate(LocalDate.parse("2020-10-12"));
        pointTracker.setEndDate(LocalDate.parse("2021-10-12"));
        pointTracker.setStatus(1);
        pointTracker.setTotalPoints("10");
        pointTracker.setActivities("Observations");
        pointTracker.setActivityIds("1");
        // Mockito.when(pointTrackerDao.getPointTrackerById(1)).thenReturn(pointTracker);
        PointTracker response = pointTrackerDao.getPointTrackerById(1);
        assertEquals(1, response.getStudyId());
    }
}