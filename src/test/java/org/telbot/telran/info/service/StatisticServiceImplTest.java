package org.telbot.telran.info.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telbot.telran.info.exceptions.NoChannelFoundException;
import org.telbot.telran.info.exceptions.NoDataFoundException;
import org.telbot.telran.info.exceptions.NoStatisticFoundException;
import org.telbot.telran.info.exceptions.NoUserFoundException;
import org.telbot.telran.info.model.Event;
import org.telbot.telran.info.model.Statistic;
import org.telbot.telran.info.repository.EventRepository;
import org.telbot.telran.info.repository.StatisticRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class StatisticServiceImplTest {

    @Autowired
    private StatisticRepository statisticRepository;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() throws NoUserFoundException, NoChannelFoundException {
        statisticRepository.deleteAll();
        eventRepository.deleteAll();

        Event eventFirst = new Event();
        eventFirst.setUserId(1);
        eventFirst.setDate("firstDate");
        eventFirst.setMessage("firstEvent");
        eventFirst.setToSend(true);
        eventRepository.save(eventFirst);
        Event eventSecond = new Event();
        eventSecond.setUserId(2);
        eventSecond.setDate("secondDate");
        eventSecond.setMessage("secondEvent");
        eventSecond.setToSend(true);
        eventRepository.save(eventSecond);
        Event eventThird = new Event();
        eventThird.setUserId(3);
        eventThird.setDate("thirdDate");
        eventThird.setMessage("thirdEvent");
        eventThird.setToSend(false);
        eventRepository.save(eventThird);
    }

    @Test
    void createReport() throws NoDataFoundException {
        statisticService.createReport();
        List<Statistic> all = statisticRepository.findAll();
        assertEquals(1, all.size());
        String reportText = "За истекший период всего подготовлено " +
                "3" +
                " events." +
                "Из них отправлено: " +
                "1" +
                ", и еще " + "2" + " пока не отправлено. " +
                "Всего " + "1" + " пользователей получили events.";
        assertEquals(reportText, all.get(0).getReport());
    }

    @Test
    void createReportWithoutNewEvents() throws NoDataFoundException {
        statisticService.createReport();
        List<Statistic> all = statisticRepository.findAll();
        assertEquals(1, all.size());
        NoDataFoundException thrown = Assertions.assertThrows(NoDataFoundException.class, () -> {
            statisticService.createReport();
        });
        assertEquals("No data found", thrown.getMessage());
        Event eventFourth = new Event();
        eventFourth.setUserId(4);
        eventFourth.setDate("fourthDate");
        eventFourth.setMessage("fourthEvent");
        eventFourth.setToSend(true);
        eventRepository.save(eventFourth);
        statisticService.createReport();
        List<Statistic> allAfterNewEvent = statisticRepository.findAll();
        assertEquals(2, allAfterNewEvent.size());
    }

    @Test
    void getLastStatisticReport() throws NoDataFoundException, NoStatisticFoundException {
        statisticService.createReport();
        Statistic lastStatisticReport = statisticService.getLastStatisticReport();
        assertEquals(1,lastStatisticReport.getId());
        Event eventFourth = new Event();
        eventFourth.setUserId(4);
        eventFourth.setDate("fourthDate");
        eventFourth.setMessage("fourthEvent");
        eventFourth.setToSend(true);
        eventRepository.save(eventFourth);
        statisticService.createReport();
        Statistic lastAgainStatisticReport = statisticService.getLastStatisticReport();
        assertEquals(2,lastAgainStatisticReport.getId());
    }
}