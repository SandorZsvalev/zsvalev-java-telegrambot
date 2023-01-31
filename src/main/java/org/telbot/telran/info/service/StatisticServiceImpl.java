package org.telbot.telran.info.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telbot.telran.info.exceptions.NoStatisticFoundException;
import org.telbot.telran.info.model.Event;
import org.telbot.telran.info.model.Statistic;
import org.telbot.telran.info.repository.StatisticRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StatisticServiceImpl implements StatisticService {

    private Logger log = LoggerFactory.getLogger(StatisticServiceImpl.class);
    @Autowired
    private StatisticRepository statisticRepository;
    @Autowired
    private EventService eventService;

    @Scheduled(fixedRateString = "${method.schedule_statistic}")
    @Override
    public void createReport() {
        int lastEventId = getLastEventId();
        int neuEventId = getNewEventId();
        if (neuEventId <= lastEventId) {
            log.error("no new events found");
            return;
        }
        List<Event> listAllEventsForReport = getListAllEventsForReport(lastEventId);
        //сколько всего event было сделано
        int totalEventForPeriod = listAllEventsForReport.size();
        //сколько из них уже отправлено
        int sentEvents = (int) listAllEventsForReport.stream().filter(event -> !event.isToSend()).count();
        int eventsToSend = totalEventForPeriod - sentEvents;
        //сколько пользователей получили евенты
        int userGetEvents = (int) listAllEventsForReport.stream()
                .filter(event -> !event.isToSend())
                .map(Event::getUserId)
                .distinct()
                .count();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("За истекший период всего подготовлено ")
                .append(totalEventForPeriod)
                .append(" events.")
                .append("Из них отправлено: ")
                .append(sentEvents)
                .append(", и еще ").append(eventsToSend).append(" пока не отправлено. ")
                .append("Всего ").append(userGetEvents).append(" пользователей получили events.");

        Statistic statistic = new Statistic();
        statistic.setLastEventId(getNewEventId());
        statistic.setReport(stringBuilder.toString());
        statisticRepository.save(statistic);
    }

    private int getLastEventId() {
        Optional<Statistic> lastEvId = statisticRepository.findTopByOrderByLastEventIdDesc();
        if (lastEvId.isPresent()) {
            Statistic statistic = lastEvId.get();
            return statistic.getLastEventId();
        } else {
            return 0;
        }
    }

    public Statistic getLastStatisticReport() throws NoStatisticFoundException {
        Optional<Statistic> report = statisticRepository.findTopByOrderByIdDesc();
        return report.orElseThrow(NoStatisticFoundException::new);
    }

    private int getNewEventId() {
        return eventService.findLastEventId();
    }

    private List<Event> getListAllEventsForReport(int from) {
        return eventService.findAllEventByIdGreaterThan(from);
    }
}
